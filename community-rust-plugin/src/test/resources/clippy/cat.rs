// Copyright (c) 2022 The Quantii Contributors
//
// This file is part of Quantii.
//
// Quantii is free software: you can redistribute
// it and/or modify it under the terms of the GNU
// Lesser General Public License as published by
// the Free Software Foundation, either version 3
// of the License, or (at your option) any later
// version.
//
// Quantii is distributed in the hope that it
// will be useful, but WITHOUT ANY WARRANTY;
// without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR
// PURPOSE. See the GNU Lesser General Public
// License for more details.
//
// You should have received a copy of the GNU
// Lesser General Public License along with
// Quantii. If not, see <https://www.gnu.org/licenses/>.

//! Core parser for Quantii Shell (Qiish).

// section clippy
#![warn(
    clippy::all,
    clippy::restriction,
    clippy::pedantic,
    clippy::nursery,
    clippy::cargo
)]
#![allow(clippy::implicit_return)]
#![allow(clippy::missing_inline_in_public_items)]
#![allow(clippy::print_stdout)]
#![allow(clippy::blanket_clippy_restriction_lints)]
#![allow(clippy::unwrap_used)]
#![allow(clippy::let_underscore_drop)]
#![allow(clippy::indexing_slicing)]
#![allow(clippy::inline_always)]
#![allow(clippy::unwrap_in_result)]
#![allow(clippy::as_conversions)]
#![allow(clippy::integer_arithmetic)]
#![allow(clippy::cast_possible_truncation)]
#![allow(clippy::panic)]
#![allow(dead_code)]
#![allow(clippy::unseparated_literal_suffix)]

//use qiish_lex::{Lexer, Token, TokenType};
use std::ops::Range;

/// The core parser for Qiish.
struct Parser {
    /// The lexer used by the parser
    lexer: Lexer,
    /// The outputted stream of tokens
    output: ParsedTokenStream,
    /// Stack of curly braces
    brace_stack: BraceStack,
    /// Stack of brackets
    bracket_stack: BracketStack,
    /// Stack of parentheses
    paren_stack: ParenthesisStack,
    /// Stack of double brackets
    double_bracket_stack: DoubleBracketStack,
    /// Stack of double parentheses
    double_paren_stack: DoubleParenthesisStack,
}

/// A token, in a syntactic context, that can be parsed further.
#[derive(Debug, Clone)]
struct ParsedToken {
    /// The token itself
    base_token: Token,
    /// The token on the left, in case of an `base_token` being an operator.
    left_operand: Option<&'static ParsedToken>,
    /// The token on the right, in case of an `base_token` being an operator.
    right_operand: Option<&'static ParsedToken>,
}

/// A stream of syntactic tokens.
///
/// Can only be used as a `mut`
#[derive(Debug)]
struct ParsedTokenStream {
    /// Array of the tokens
    tokens: Vec<ParsedToken>,
    /// Current index of the token stream
    index: u64,
}

impl ParsedTokenStream {
    /// Creates a new token stream.
    pub const fn new() -> Self {
        Self {
            tokens: vec![],
            index: 0,
        }
    }

    /// Adds a token to the stream.
    pub fn push(&mut self, tk: ParsedToken) {
        self.tokens.push(tk);
    }

    /// Returns the next token.
    pub fn next(&mut self) -> ParsedToken {
        let c: ParsedToken = self.tokens[self.index as usize].clone();
        self.index += 1;
        c
    }

    /// Checks if there are more tokens in the stream.
    pub fn has_next(&self) -> bool {
        self.index < self.tokens.len() as u64
    }

    /// Returns the next token without consuming it.
    pub fn lookahead(&self, index: u64) -> ParsedToken {
        self.tokens[(self.index + index) as usize].clone()
    }

    /// Change the left operand of a token in the stream.
    pub fn change_left_operand(&mut self, index: usize, left_op_tk: &'static ParsedToken) {
        self.tokens[index].left_operand = Some(left_op_tk);
    }

    /// Change the right operand of a token in the stream.
    pub fn change_right_operand(&mut self, index: usize, right_op_tk: &'static ParsedToken) {
        self.tokens[index].right_operand = Some(right_op_tk);
    }
}

/// A stack of braces, brackets, parenthesis, double brackets, and/or double parenthesis.
trait Stack {
    /// Push a new stack layer
    fn push(&mut self);
    /// Pop a stack layer
    fn pop(&mut self);
    /// Get current stack depth
    fn peek(&self) -> i64;
}

/// Stack of braces
struct BraceStack {
    /// Vector
    stack: Vec<Token>,
    /// Current layer
    brace_layer: i64,
}
impl Stack for BraceStack {
    fn push(&mut self) {
        self.brace_layer += 1;
    }
    fn pop(&mut self) {
        self.brace_layer -= 1;
    }
    fn peek(&self) -> i64 {
        self.brace_layer
    }
}

/// Stack of brackets
struct BracketStack {
    /// Vector
    stack: Vec<Token>,
    /// Current layer
    bracket_layer: i64,
}
impl Stack for BracketStack {
    fn push(&mut self) {
        self.bracket_layer += 1;
    }
    fn pop(&mut self) {
        self.bracket_layer -= 1;
    }
    fn peek(&self) -> i64 {
        self.bracket_layer
    }
}

/// Stack of parenthesis
struct ParenthesisStack {
    /// Vector
    stack: Vec<Token>,
    /// Current layer
    parenthesis_layer: i64,
}
impl Stack for ParenthesisStack {
    fn push(&mut self) {
        self.parenthesis_layer += 1;
    }
    fn pop(&mut self) {
        self.parenthesis_layer -= 1;
    }
    fn peek(&self) -> i64 {
        self.parenthesis_layer
    }
}

/// Stack of double brackets
struct DoubleBracketStack {
    /// Vector
    stack: Vec<Token>,
    /// Current layer
    double_bracket_layer: i64,
}
impl Stack for DoubleBracketStack {
    fn push(&mut self) {
        self.double_bracket_layer += 1;
    }
    fn pop(&mut self) {
        self.double_bracket_layer -= 1;
    }
    fn peek(&self) -> i64 {
        self.double_bracket_layer
    }
}

/// Stack of double parenthesis
struct DoubleParenthesisStack {
    /// Vector
    stack: Vec<Token>,
    /// Current layer
    double_parenthesis_layer: i64,
}
impl Stack for DoubleParenthesisStack {
    fn push(&mut self) {
        self.double_parenthesis_layer += 1;
    }
    fn pop(&mut self) {
        self.double_parenthesis_layer -= 1;
    }
    fn peek(&self) -> i64 {
        self.double_parenthesis_layer
    }
}

impl Parser {
    /// Creates a new parser.
    pub const fn new(lexer: Lexer) -> Self {
        Self {
            lexer,
            output: ParsedTokenStream::new(),
            brace_stack: BraceStack {
                stack: vec![],
                brace_layer: 0,
            },
            bracket_stack: BracketStack {
                stack: vec![],
                bracket_layer: 0,
            },
            paren_stack: ParenthesisStack {
                stack: vec![],
                parenthesis_layer: 0,
            },
            double_bracket_stack: DoubleBracketStack {
                stack: vec![],
                double_bracket_layer: 0,
            },
            double_paren_stack: DoubleParenthesisStack {
                stack: vec![],
                double_parenthesis_layer: 0,
            },
        }
    }

    /// Parses the input.
    pub fn parse(&mut self) {
        while self.lexer.output.has_next() {
            let in_token: &Token = self.lexer.output.next();

            match in_token.get_type() {
                TokenType::Redir => {}
                TokenType::RedirAppend => {}
                TokenType::ForceRedir => {}

                TokenType::Pipe => {}

                TokenType::Case => {}
                TokenType::Coproc => {}
                TokenType::Do => {}
                TokenType::Done => {}
                TokenType::Elif => {}
                TokenType::Else => {}
                TokenType::Esac => {}
                TokenType::Fi => {}
                TokenType::For => {}
                TokenType::Function => {}
                TokenType::If => {}
                TokenType::In => {}
                TokenType::Select => {}
                TokenType::Then => {}
                TokenType::Time => {}
                TokenType::Until => {}
                TokenType::While => {}

                TokenType::LeftCurlyBrace => {
                    self.brace_stack.push();
                }
                TokenType::RightCurlyBrace => {
                    self.brace_stack.pop();
                }
                TokenType::LeftSquareBracket => {
                    self.bracket_stack.push();
                }
                TokenType::RightSquareBracket => {
                    self.bracket_stack.pop();
                }
                TokenType::LeftParen => {
                    self.paren_stack.push();
                }
                TokenType::RightParen => {
                    self.paren_stack.pop();
                }

                TokenType::LeftDoubleSquareBracket => {
                    self.double_bracket_stack.push();
                }
                TokenType::RightDoubleSquareBracket => {
                    self.double_bracket_stack.pop();
                }
                TokenType::LeftDoubleParen => {
                    self.double_paren_stack.push();
                }
                TokenType::RightDoubleParen => {
                    self.double_paren_stack.pop();
                }

                TokenType::Text => {}

                TokenType::EOF => {
                    // Reached end of file
                }
            }
        }
    }
}

// Copyright (c) 2022 The Quantii Contributors
//
// This file is part of Quantii.
//
// Quantii is free software: you can redistribute
// it and/or modify it under the terms of the GNU
// Lesser General Public License as published by
// the Free Software Foundation, either version 3
// of the License, or (at your option) any later
// version.
//
// Quantii is distributed in the hope that it
// will be useful, but WITHOUT ANY WARRANTY;
// without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR
// PURPOSE. See the GNU Lesser General Public
// License for more details.
//
// You should have received a copy of the GNU
// Lesser General Public License along with
// Quantii. If not, see <https://www.gnu.org/licenses/>.
// section clippy




/// The type of a token, stored in the [`Token`].
#[derive(Debug, Clone, Copy, PartialEq, Eq)]
pub enum TokenType {
    // Redirection
    /// `>`
    Redir,
    /// `>>`
    RedirAppend,
    /// `>|`
    ForceRedir,
    /// `|`
    Pipe,

    // Keywords
    /// `case`
    Case,
    /// `coproc`
    Coproc,
    /// `do`
    Do,
    /// `done`
    Done,
    /// `elif`
    Elif,
    /// `else`
    Else,
    /// `esac`
    Esac,
    /// `fi`
    Fi,
    /// `for`
    For,
    /// `function`
    Function,
    /// `if`
    If,
    /// `in`
    In,
    /// `select`
    Select,
    /// `then`
    Then,
    /// `time`
    Time,
    /// `until`
    Until,
    /// `while`
    While,

    // Brackets and braces
    /// `{`
    LeftCurlyBrace,
    /// `}`
    RightCurlyBrace,
    /// `[`
    LeftSquareBracket,
    /// `]`
    RightSquareBracket,
    /// `(`
    LeftParen,
    /// `)`
    RightParen,

    // Double brackets and braces
    /// `[[`
    LeftDoubleSquareBracket,
    /// `]]`
    RightDoubleSquareBracket,
    /// `((`
    LeftDoubleParen,
    /// `))`
    RightDoubleParen,

    // Other
    /// Any other character patterns
    Text,
    /// End of file
    EOF
}

/// A token generated by the Quantii Shell Lexer (Qiish-lex).
#[derive(Debug, Clone, PartialEq)]
pub struct Token {
    /// The type of the token.
    ttype: TokenType,
}

impl Token {
    /// Create a new [`Token`] with the given [`TokenType`] and value(s).
    ///
    /// # Arguments
    ///
    /// * `ttype`: [`TokenType`] - The type of the token.
    /// * `token_value`: [`Vec<Token>`] - The token-like value of the token.
    /// * `string_value`: [`Vec<String>`] - The string-like value of the token.
    ///
    /// returns: [`Token`]
    #[must_use]
    pub const fn new(ttype: TokenType) -> Self {
        Self { ttype }
    }

    /// Get the type of the current [`Token`].
    ///
    /// returns: [`TokenType`]
    pub const fn get_type(&self) -> &TokenType {
        &self.ttype
    }
}

/// A stream of [`Token`]s generated by the Quantii
/// Shell Lexer (Qiish-lex) and read by the Quantii
/// Shell Parser (Qiish-parse).
#[derive(Debug)]
pub struct TokenStream {
    /// A list of all the tokens in the stream
    tokens: Vec<Token>,
    /// The current position in the stream
    index: u32,
}

impl TokenStream {
    /// Create a new [`TokenStream`] with the given [`Vec<Token>`].
    ///
    /// returns: [`TokenStream`]
    pub const fn new() -> Self {
        Self {
            tokens: vec![],
            index: 0,
        }
    }

    /// Get the index of the current [`char`]
    pub fn get_index(&mut self) -> &u32 {
        &self.index
    }
    
    /// Add a new [`Token`] to the stream.
    ///
    /// # Arguments
    /// * token: [`Token`] - The token to add to the stream.
    ///
    /// returns: [`()`]
    pub fn push(&mut self, token: Token) {
        self.tokens.push(token);
    }

    /// Get the next [`Token`] in the stream.
    ///
    /// returns: `&`[`Token`]
    pub fn next(&mut self) -> &Token {
        let token = &self.tokens[self.index as usize];
        self.index += 1;
        token
    }

    /// Checks if there is another [`Token`] in the stream.
    ///
    /// returns: [`bool`]
    pub fn has_next(&self) -> bool {
        self.index < self.tokens.len() as u32
    }

    /// Gets a specific [`Token`] in the stream, without eating it.
    ///
    /// # Arguments
    /// * index: [`u32`] - The index of the token to get.
    pub fn lookahead(&self, index: u32) -> &Token {
        &self.tokens[(self.index + index) as usize]
    }
}

/// A stream of [`char`]s.
#[derive(Debug, Default)]
pub struct CharStream {
    /// A list of all the characters in the stream
    chars: Vec<char>,
    /// The current position in the stream
    index: u64,
}

impl CharStream {
    /// Create a new [`CharStream`].
    ///
    /// returns: [`CharStream`]
    pub const fn new() -> Self {
        Self {
            chars: vec![],
            index: 0,
        }
    }
    
    /// Get the index of the current [`char`]
    pub fn get_index(&mut self) -> &u64 {
        &self.index
    }

    /// Add a new [`char`] to the stream.
    ///
    /// # Arguments
    /// * c: [`char`] - The character to add to the stream.
    ///
    /// returns: [`()`]
    pub fn push(&mut self, c: char) {
        self.chars.push(c);
    }

    /// Get the next [`char`] in the stream.
    ///
    /// returns: [`char`]
    pub fn next(&mut self) -> char {
        let c = self.chars[self.index as usize];
        self.index += 1;
        c
    }

    /// Checks if there is another [`char`] in the stream.
    ///
    /// returns: [`bool`]
    pub fn has_next(&self) -> bool {
        self.index < self.chars.len() as u64
    }

    /// Gets a specific [`char`] in the stream, without eating it.
    ///
    /// # Arguments
    /// * index: [`u64`] - The index of the character to get.
    ///
    /// returns: [`char`]
    pub fn lookahead(&self, index: u64) -> char {
        self.chars[(self.index + index) as usize]
    }

    /// Gets a specific range of [`char`]s in the stream,
    /// without eating them.
    ///
    /// Inclusive of the start and end indices.
    ///
    /// # Arguments
    /// * start: [`u32`] - The index of the first character to get.
    /// * end: [`u32`] - The index of the last character to get.
    pub fn lookahead_range(&self, start: u32, end: u32) -> String {
        let mut ret: String = "".to_owned();
        for i in start..=end {
            ret = format!("{}{}", ret, &self.chars[i as usize]);
        }
        ret
    }

    /// Look ahead multiple characters.
    pub fn lookahead_multi(&self, count: u64) -> String {
        let mut ret: String = "".to_owned();
        for i in self.index..=(self.index + count) {
            ret = format!("{}{}", ret, &self.chars[i as usize]);
        }
        ret
    }

    /// Check if there are multiple next characters.
    pub fn has_next_multi(&self, count: u64) -> bool {
        self.index + count <= self.chars.len() as u64
    }
}

impl From<String> for CharStream {
    fn from(s: String) -> Self {
        let mut c_stream: Self = Self::new();
        for i in s.chars() {
            c_stream.push(i);
        }
        c_stream
    }
}

impl From<&str> for CharStream {
    fn from(s: &str) -> Self {
        let mut c_stream: Self = Self::new();
        for i in s.chars() {
            c_stream.push(i);
        }
        c_stream
    }
}

/// The Quantii Shell Lexer (Qiish-lex).
pub struct Lexer {
    /// The stream of characters to lex.
    input: CharStream,
    /// The output tokens to be used by
    /// the Quantii Shell Parser (Qiish-parse).
    pub output: TokenStream,
}

impl Lexer {
    /// Create a new [`Lexer`] with the given &[`str`].
    ///
    /// # Arguments
    /// * input: &[`str`] - The input string to lex.
    ///
    /// returns: [`Lexer`]
    #[must_use]
    pub fn new(input: &str) -> Self {
        Self {
            input: CharStream::from(input.to_owned()),
            output: TokenStream::new(),
        }
    }

    /// Lex the input string into a [`TokenStream`].
    ///
    /// # Panics
    /// Panics if it receives a character it doesn't understand.
    pub fn lex(&mut self) {
        while self.input.has_next() {
            let c: char = self.input.next();
            match c {
                /// Any whitespace is ignored.
                ' ' | '\t' | '\n' | '\r' => {
                    continue;
                }

                /// Comments can also be ignored.
                '#' => {
                    while self.input.has_next() && self.input.next() != '\n' {
                        self.input.next();
                    }
                    continue;
                }

                // Redirection
                '>' => {
                    if self.input.has_next() {
                        match self.input.lookahead(1) {
                            '>' => {
                                self.input.next();
                                self.output.push(Token::new(TokenType::RedirAppend));
                            }

                            '|' => {
                                self.input.next();
                                self.output.push(Token::new(TokenType::ForceRedir));
                            }

                            _ => {
                                // We shouldn't eat the next token if it's anything else,
                                // it just means that the code either has a space afterwords
                                // or it's (potentially) important text
                                self.output.push(Token::new(TokenType::Redir));
                            }
                        }
                    }
                }

                // Pipes
                '|' => {
                    self.output.push(Token::new(TokenType::Pipe));
                }

                '{' => {
                    self.output.push(Token::new(TokenType::LeftCurlyBrace));
                }

                '}' => {
                    self.output.push(Token::new(TokenType::RightCurlyBrace));
                }

                '[' => {
                    if self.input.has_next() && self.input.lookahead(1) == '[' {
                        self.input.next();
                        self.output
                            .push(Token::new(TokenType::LeftDoubleSquareBracket));
                    } else {
                        self.output.push(Token::new(TokenType::LeftSquareBracket));
                    }
                }

                ']' => {
                    if self.input.has_next() && self.input.lookahead(1) == ']' {
                        self.input.next();
                        self.output
                            .push(Token::new(TokenType::RightDoubleSquareBracket));
                    } else {
                        self.output.push(Token::new(TokenType::RightSquareBracket));
                    }
                }

                '(' => {
                    if self.input.has_next() && self.input.lookahead(1) == '(' {
                        self.input.next();
                        self.output.push(Token::new(TokenType::LeftDoubleParen));
                    } else {
                        self.output.push(Token::new(TokenType::LeftParen));
                    }
                }
                ')' => {
                    if self.input.has_next() && self.input.lookahead(1) == ')' {
                        self.input.next();
                        self.output.push(Token::new(TokenType::RightDoubleParen));
                    } else {
                        self.output.push(Token::new(TokenType::RightParen));
                    }
                }

                // Keywords and identifiers
                'a'..='z' | 'A'..='Z' | '_' => {
                    if self.input.has_next_multi(3)
                        && c == 'c'
                        && self.input.lookahead_multi(3) == "ase"
                    {
                        self.input.next();
                        self.input.next();
                        self.output.push(Token::new(TokenType::Case));
                    } else if self.input.has_next_multi(5)
                        && c == 'c'
                        && self.input.lookahead_multi(5) == "oproc"
                    {
                        self.input.next();
                        self.input.next();
                        self.input.next();
                        self.input.next();
                        self.input.next();
                        self.output.push(Token::new(TokenType::Coproc));
                    } else if self.input.has_next() && c == 'd' && self.input.lookahead(1) == 'o' {
                        self.input.next();
                        self.output.push(Token::new(TokenType::Do));
                    } else if self.input.has_next_multi(3)
                        && c == 'd'
                        && self.input.lookahead_multi(3) == "one"
                    {
                        self.input.next();
                        self.input.next();
                        self.input.next();
                        self.output.push(Token::new(TokenType::Done));
                    } else if self.input.has_next_multi(3)
                        && c == 'e'
                        && self.input.lookahead_multi(3) == "lif"
                    {
                        self.input.next();
                        self.input.next();
                        self.input.next();
                        self.output.push(Token::new(TokenType::Elif));
                    } else if self.input.has_next_multi(3)
                        && c == 'e'
                        && self.input.lookahead_multi(3) == "lse"
                    {
                        self.input.next();
                        self.input.next();
                        self.input.next();
                        self.output.push(Token::new(TokenType::Else));
                    } else if self.input.has_next_multi(3)
                        && c == 'e'
                        && self.input.lookahead_multi(3) == "sac"
                    {
                        self.input.next();
                        self.input.next();
                        self.input.next();
                        self.output.push(Token::new(TokenType::Esac));
                    } else if self.input.has_next() && c == 'f' && self.input.lookahead(1) == 'i' {
                        self.input.next();
                        self.output.push(Token::new(TokenType::Fi));
                    } else if self.input.has_next_multi(2)
                        && c == 'f'
                        && self.input.lookahead_multi(2) == "or"
                    {
                        self.input.next();
                        self.input.next();
                        self.output.push(Token::new(TokenType::For));
                    } else if self.input.has_next_multi(7)
                        && c == 'f'
                        && self.input.lookahead_multi(7) == "unction"
                    {
                        self.input.next();
                        self.input.next();
                        self.input.next();
                        self.input.next();
                        self.input.next();
                        self.input.next();
                        self.input.next();
                        self.output.push(Token::new(TokenType::Function));
                    } else if self.input.has_next() && c == 'i' && self.input.lookahead(1) == 'f' {
                        self.input.next();
                        self.output.push(Token::new(TokenType::If));
                    } else if self.input.has_next() && c == 'i' && self.input.lookahead(1) == 'n' {
                        self.input.next();
                        self.output.push(Token::new(TokenType::In));
                    } else if self.input.has_next_multi(5)
                        && c == 's'
                        && self.input.lookahead_multi(5) == "elect"
                    {
                        self.input.next();
                        self.input.next();
                        self.input.next();
                        self.input.next();
                        self.input.next();
                        self.output.push(Token::new(TokenType::Select));
                    } else if self.input.has_next_multi(3)
                        && c == 't'
                        && self.input.lookahead_multi(3) == "hen"
                    {
                        self.input.next();
                        self.input.next();
                        self.input.next();
                        self.output.push(Token::new(TokenType::Then));
                    } else if self.input.has_next_multi(3)
                        && c == 't'
                        && self.input.lookahead_multi(3) == "ime"
                    {
                        self.input.next();
                        self.input.next();
                        self.input.next();
                        self.output.push(Token::new(TokenType::Time));
                    } else if self.input.has_next_multi(4)
                        && c == 'u'
                        && self.input.lookahead_multi(4) == "ntil"
                    {
                        self.input.next();
                        self.input.next();
                        self.input.next();
                        self.input.next();
                        self.output.push(Token::new(TokenType::Until));
                    } else if self.input.has_next_multi(4)
                        && c == 'w'
                        && self.input.lookahead_multi(4) == "hile"
                    {
                        self.input.next();
                        self.input.next();
                        self.input.next();
                        self.input.next();
                        self.output.push(Token::new(TokenType::While));
                    } else {
                        let mut tk: String = "".to_owned();
                        while self.input.has_next() {
                            match self.input.next() {
                                'a'..='z' | 'A'..='Z' | '_' | '0'..='9' => {
                                    tk.push(c);
                                    self.input.next();
                                }
                                _ => {
                                    break;
                                }
                            }
                            self.output.push(Token::new(TokenType::Text));
                        }
                    }
                }

                _ => {
                    panic!("Can't handle character {}", c)
                }
            }
            
            self.output.push(Token::new(TokenType::EOF));
        }
    }
}