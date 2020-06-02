/*
 * Sonar C++ Plugin (Community)
 * Copyright (C) 2010-2020 SonarOpenCommunity
 * http://github.com/SonarOpenCommunity/sonar-Rust
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package org.elegoff.rust.parser;

import static com.sonar.sslr.api.GenericTokenType.EOF;
import static com.sonar.sslr.api.GenericTokenType.IDENTIFIER;
import static org.elegoff.rust.api.RustTokenType.*;

import com.sonar.sslr.api.Grammar;

import org.elegoff.rust.RustSquidConfiguration;
import org.elegoff.rust.api.RustKeyword;
import org.sonar.sslr.grammar.GrammarRuleKey;
import org.sonar.sslr.grammar.LexerfulGrammarBuilder;

/**
 * Parsing expression grammar (PEG)[https://en.wikipedia.org/wiki/Parsing_expression_grammar]
 *
 * The fundamental difference between context-free grammars and parsing expression grammars is that the PEG's choice
 * operator is ordered. If the first alternative succeeds, the second alternative is ignored. The consequence is that if
 * a CFG is transliterated directly to a PEG, any ambiguity in the former is resolved by deterministically picking one
 * parse tree from the possible parses.
 *
 * By carefully choosing the order in which the grammar alternatives are specified, a programmer has a great deal of
 * control over which parse tree is selected.
 *
 */
/**
 * Based on the C++ Standard, Appendix A
 */
@SuppressWarnings({"squid:S00115", "squid:S00103"})
public enum RustGrammarImpl implements GrammarRuleKey {

    // Misc
    BOOL,
    NULLPTR,
    LITERAL,
    // Top level components
    translationUnit,
    // Expressions
    primaryExpression,
    idExpression,
    unqualifiedId,
    qualifiedId,
    nestedNameSpecifier,
    lambdaExpression,
    lambdaIntroducer,
    lambdaCapture,
    captureDefault,
    captureList,
    capture,
    simpleCapture,
    initCapture,
    lambdaDeclarator,
    foldExpression,
    foldOperator,
    postfixExpression,
    expressionList,
    pseudoDestructorName,
    unaryExpression,
    unaryOperator,
    binaryOperator,
    newExpression,
    newPlacement,
    newTypeId,
    newDeclarator,
    noptrNewDeclarator,
    newInitializer,
    deleteExpression,
    noexceptExpression,
    castExpression,
    pmExpression,
    multiplicativeExpression,
    additiveExpression,
    shiftExpression,
    relationalExpression,
    equalityExpression,
    andExpression,
    exclusiveOrExpression,
    inclusiveOrExpression,
    logicalAndExpression,
    logicalOrExpression,
    conditionalExpression,
    assignmentExpression,
    assignmentOperator,
    expression,
    constantExpression,
    // Statements
    statement,
    emptyStatement,
    labeledStatement,
    expressionStatement,
    compoundStatement,
    statementSeq,
    condition,
    selectionStatement,
    iterationStatement,
    initStatement,
    forRangeDeclaration,
    forRangeInitializer,
    jumpStatement,
    declarationStatement,
    // Declarations
    declarationSeq,
    declaration,
    blockDeclaration,
    nodeclspecFunctionDeclaration,
    aliasDeclaration,
    simpleDeclaration,
    staticAssertDeclaration,
    emptyDeclaration,
    attributeDeclaration,
    declSpecifier,
    recoveredDeclaration,
    conditionDeclSpecifierSeq,
    forRangeDeclSpecifierSeq,
    parameterDeclSpecifierSeq,
    functionDeclSpecifierSeq,
    declSpecifierSeq,
    memberDeclSpecifierSeq,
    identifierList,
    storageClassSpecifier,
    functionSpecifier,
    typedefName,
    typeSpecifier,
    typeSpecifierSeq,
    definingTypeSpecifier,
    definingTypeSpecifierSeq,
    trailingTypeSpecifier,
    simpleTypeSpecifier,
    typeName,
    decltypeSpecifier,
    elaboratedTypeSpecifier,
    enumName,
    enumSpecifier,
    enumHead,
    enumHeadName,
    opaqueEnumDeclaration,
    enumKey,
    enumBase,
    enumeratorList,
    enumeratorDefinition,
    enumerator,
    namespaceName,
    namespaceDefinition,
    namedNamespaceDefinition,
    unnamedNamespaceDefinition,
    nestedNamespaceDefinition,
    enclosingNamespaceSpecifier,
    namespaceBody,
    namespaceAlias,
    namespaceAliasDefinition,
    qualifiedNamespaceSpecifier,
    usingDeclaration,
    usingDeclaratorList,
    usingDeclarator,
    usingDirective,
    asmDefinition,
    asmLabel,
    linkageSpecification,
    attributeSpecifierSeq,
    attributeSpecifier,
    alignmentSpecifier,
    attributeUsingPrefix,
    attributeList,
    attribute,
    attributeToken,
    attributeScopedToken,
    attributeNamespace,
    attributeArgumentClause,
    balancedTokenSeq,
    balancedToken,
    // Declarators
    initDeclaratorList,
    initDeclarator,
    declarator,
    ptrDeclarator,
    noptrDeclarator,
    parametersAndQualifiers,
    trailingReturnType,
    ptrOperator,
    cvQualifierSeq,
    cvQualifier,
    refQualifier,
    declaratorId,
    typeId,
    definingTypeId,
    typeIdEnclosed,
    abstractDeclarator,
    ptrAbstractDeclarator,
    noptrAbstractDeclarator,
    abstractPackDeclarator,
    noptrAbstractPackDeclarator,
    parameterDeclarationClause,
    parameterDeclarationList,
    parameterDeclaration,
    functionDefinition,
    functionBody,
    initializer,
    braceOrEqualInitializer,
    initializerClause,
    initializerList,
    bracedInitList,
    exprOrBracedInitList,
    // Classes
    className,
    classSpecifier,
    classHead,
    classHeadName,
    classVirtSpecifier,
    classKey,
    memberSpecification,
    memberDeclaration,
    memberDeclaratorList,
    memberDeclarator,
    virtSpecifierSeq,
    virtSpecifier,
    pureSpecifier,
    // Derived classes
    baseClause,
    baseSpecifierList,
    baseSpecifier,
    classOrDecltype,
    baseTypeSpecifier,
    accessSpecifier,
    // Special member functions
    conversionFunctionId,
    conversionTypeId,
    conversionDeclarator,
    ctorInitializer,
    memInitializerList,
    memInitializer,
    memInitializerId,
    // Overloading
    operatorFunctionId,
    overloadableOperator,
    literalOperatorId,
    // Templates
    templateDeclaration,
    templateParameterList,
    templateParameterListEnclosed,
    templateParameter,
    typeParameter,
    typeParameterKey,
    innerTypeParameter,
    simpleTemplateId,
    templateId,
    templateName,
    templateArgumentList,
    templateArgument,
    innerSimpleTemplateId,
    innerTrailingTypeSpecifier,
    innerTypeId,
    innerTemplateId,
    innerTemplateArgumentList,
    innerTemplateArgument,
    typenameSpecifier,
    explicitInstantiation,
    explicitSpecialization,
    deductionGuide,
    // Exception handling
    tryBlock,
    functionTryBlock,
    handlerSeq,
    handler,
    exceptionDeclaration,
    throwExpression,
    typeIdList,
    noexceptSpecifier,
    // Microsoft Extension: C++/CLI
    cliTopLevelVisibility,
    cliFinallyClause,
    cliFunctionModifiers,
    cliFunctionModifier,
    cliEventDefinition,
    cliEventModifiers,
    cliPropertyOrEventName,
    cliEventType,
    cliParameterArray,
    cliPropertyDefinition,
    cliPropertyDeclSpecifierSeq,
    cliPropertyBody,
    cliPropertyModifiers,
    cliPropertyIndexes,
    cliPropertyIndexParameterList,
    cliAccessorSpecification,
    cliAccessorDeclaration,
    cliDelegateSpecifier,
    cliDelegateDeclSpecifierSeq,
    cliGenericDeclaration,
    cliGenericParameterList,
    cliConstraintClauseList,
    cliConstraintItemList,
    cliGenericParameter,
    cliGenericId,
    cliGenericName,
    cliGenericArgumentList,
    cliGenericArgument,
    cliConstraintClause,
    cliConstraintItem,
    cliAttributes,
    cliAttributeSection,
    cliAttributeTargetSpecifier,
    cliAttributeTarget,
    cliAttributeList,
    cliAttribute,
    cliAttributeArguments,
    cliPositionArgumentList,
    cliNamedArgumentList,
    cliPositionArgument,
    cliNamedArgument,
    cliAttributeArgumentExpression,
    // Microsoft Extension: Attributed ATL
    vcAtlDeclaration,
    vcAtlAttribute,
    // CUDA extension
    cudaKernel;

    public static Grammar create(RustSquidConfiguration squidConfig) {
        LexerfulGrammarBuilder b = LexerfulGrammarBuilder.create();

        toplevel(b, squidConfig);
        expressions(b);
        statements(b);
        declarations(b);
        declarators(b);
        classes(b);
        properties(b);
        derivedClasses(b);
        specialMemberFunctions(b);
        overloading(b);
        templates(b);
        generics(b);
        exceptionHandling(b);
        cliAttributes(b);

        misc(b);
        vcAttributedAtl(b);

        b.setRootRule(translationUnit);

        return b.buildWithMemoizationOfMatchesForAllRules();
    }

    private static void misc(LexerfulGrammarBuilder b) {

        b.rule(identifierList).is(
                IDENTIFIER, b.zeroOrMore(",", IDENTIFIER) //C++
        );

        // C++ Boolean literals [lex.bool]
        b.rule(BOOL).is(
                b.firstOf(
                        RustKeyword.TRUE,
                        RustKeyword.FALSE
                )
        );

        // C++ Pointer literals [lex.nullptr]
        b.rule(NULLPTR).is(
                RustKeyword.NULLPTR
        );

        // C++ Kinds of literals [lex.literal.kinds]
        b.rule(LITERAL).is(
                b.firstOf(
                        CHARACTER, // character-literal, including user-defined-literal
                        STRING, // string-literal, including user-defined-string-literal
                        NUMBER, // integer-literal, floating-literal, including user-defined-literal
                        BOOL, // boolean-literal
                        NULLPTR // pointer-literal
                )
        ).skip();
    }

    private static void vcAttributedAtl(LexerfulGrammarBuilder b) {

        b.rule(vcAtlAttribute).is(
                "[", b.oneOrMore(b.anyTokenButNot("]")), "]"
        );
        b.rule(vcAtlDeclaration).is(vcAtlAttribute, ";");
    }

    // A.3 Basic concepts
    //
    private static void toplevel(LexerfulGrammarBuilder b, RustSquidConfiguration squidConfig) {

        if (squidConfig.getErrorRecoveryEnabled()) {
            b.rule(translationUnit).is(
                    b.zeroOrMore(
                            b.firstOf(
                                    declaration,
                                    recoveredDeclaration)
                    ), EOF
            );
            b.rule(recoveredDeclaration).is(
                    b.oneOrMore(
                            b.nextNot(
                                    b.firstOf(
                                            declaration,
                                            EOF
                                    )
                            ), b.anyToken()
                    )
            );
        } else {
            b.rule(translationUnit).is(
                    b.zeroOrMore(declaration), EOF
            );
        }
    }

    // A.4 Expressions
    //
    private static void expressions(LexerfulGrammarBuilder b) {
        b.rule(primaryExpression).is(
                b.firstOf(
                        LITERAL, // C++
                        RustKeyword.THIS, // C++
                        // EXTENSION: gcc's statement expression: a compound statement enclosed in parentheses may appear as an expression
                        b.sequence("(",
                                b.firstOf(
                                        expression,
                                        compoundStatement
                                ), ")"
                        ), // C++: ( expression )
                        idExpression, // C++
                        lambdaExpression, // C++
                        foldExpression // C++
                )
        ).skipIfOneChild();

        b.rule(idExpression).is(
                b.firstOf(
                        qualifiedId, // C++
                        unqualifiedId // C++ (PEG: different order)
                )
        ).skip();

        b.rule(unqualifiedId).is(
                b.firstOf(
                        // Mitigate ambiguity between relational operators < > and angular brackets by looking ahead
                        b.sequence(templateId, b.next(b.firstOf("(", ")", "[", "]", "?", ":", binaryOperator, ",", ";", "}", EOF))), // todo?
                        IDENTIFIER, // C++
                        operatorFunctionId, // C++
                        conversionFunctionId, // C++
                        literalOperatorId, // C++
                        b.sequence("~", className), // C++
                        b.sequence("~", decltypeSpecifier), // C++

                        b.sequence("!", className), // C++/CLI
                        cliGenericId, // C++/CLI
                        RustKeyword.DEFAULT // C++/CLI
                )
        ).skipIfOneChild();

        b.rule(binaryOperator).is( // todo
                b.firstOf(
                        "||", RustKeyword.OR, "&&", RustKeyword.AND, "&", RustKeyword.BITAND, "|", RustKeyword.BITOR, "^", RustKeyword.XOR,
                        "==", "!=", RustKeyword.NOT_EQ, "<=", "<", ">=", ">", "<<", ">>", "*", "/", "+", "-", assignmentOperator
                )
        );

        b.rule(qualifiedId).is(
                nestedNameSpecifier, b.optional(RustKeyword.TEMPLATE), unqualifiedId // C++
        );

        b.rule(nestedNameSpecifier).is(
                b.firstOf(
                        "::", // C++
                        b.sequence(typeName, "::"), // C++
                        b.sequence(namespaceName, "::"), // C++
                        b.sequence(decltypeSpecifier, "::") // C++
                ),
                b.zeroOrMore(
                        b.firstOf(
                                b.sequence(IDENTIFIER, "::"), // C++
                                b.sequence(b.optional(RustKeyword.TEMPLATE), simpleTemplateId, "::") // C++
                        )
                )
        );

        b.rule(lambdaExpression).is(
                lambdaIntroducer, b.optional(lambdaDeclarator), compoundStatement // C++
        );

        b.rule(lambdaIntroducer).is(
                "[", b.optional(lambdaCapture), "]" // C++
        );

        b.rule(lambdaCapture).is(
                b.firstOf(
                        b.sequence(captureDefault, b.optional(",", captureList)), // C++
                        captureList // C++
                )
        );

        b.rule(captureDefault).is(
                b.firstOf("&", "="), // C++
                b.nextNot(capture)
        );

        b.rule(captureList).is(
                b.sequence(capture, b.optional("...")), b.zeroOrMore(",", capture, b.optional("...")) // C++
        );

        b.rule(capture).is(
                b.firstOf(
                        b.sequence(simpleCapture, b.nextNot("=")), // C++
                        initCapture // C++
                )
        ).skip();

        b.rule(simpleCapture).is(
                b.firstOf(
                        IDENTIFIER, // C++
                        b.sequence("&", IDENTIFIER), // C++
                        RustKeyword.THIS, // C++
                        b.sequence("*", RustKeyword.THIS) // C++
                )
        );

        b.rule(initCapture).is(
                b.firstOf(
                        b.sequence(IDENTIFIER, initializer), // C++
                        b.sequence("&", IDENTIFIER, initializer) // C++
                )
        );

        b.rule(lambdaDeclarator).is(
                "(", parameterDeclarationClause, ")", b.optional(declSpecifierSeq), b.optional(noexceptSpecifier), b.optional(
                        attributeSpecifierSeq), b.optional(trailingReturnType) // C++
        );

        b.rule(foldExpression).is(
                b.firstOf(
                        b.sequence("(", castExpression, foldOperator, "...", ")"), // C++
                        b.sequence("(", "...", foldOperator, castExpression, ")"), // C++
                        b.sequence("(", castExpression, foldOperator, "...", foldOperator, castExpression, ")") // C++
                )
        );

        b.rule(foldOperator).is(
                b.firstOf( // C++
                        "+", "-", "*", "/", "%", "ˆ", RustKeyword.XOR, "&", RustKeyword.BITAND, "|", RustKeyword.BITOR, "<<", ">>",
                        "+=", "-=", "*=", "/=", "%=", "ˆ=", RustKeyword.XOR_EQ, "&=", RustKeyword.AND_EQ, "|=", RustKeyword.OR_EQ, "<<=",
                        ">>=", "=",
                        "==", "!=", RustKeyword.NOT_EQ, "<", ">", "<=", ">=", "&&", RustKeyword.AND, "||", RustKeyword.OR, ",", ".*", "->*"
                )
        );

        b.rule(postfixExpression).is(
                b.firstOf(
                        b.sequence(
                                typenameSpecifier,
                                b.firstOf(
                                        b.sequence("::", RustKeyword.TYPEID), // C++/CLI
                                        b.sequence(b.optional(cudaKernel), "(", b.optional(expressionList), ")"), // C++
                                        bracedInitList // C++
                                )
                        ),
                        b.sequence(
                                simpleTypeSpecifier,
                                b.firstOf(
                                        b.sequence("::", RustKeyword.TYPEID), // C++/CLI
                                        b.sequence(b.optional(cudaKernel), "(", b.optional(expressionList), ")"), // C++
                                        bracedInitList // C++
                                )
                        ),
                        primaryExpression, // C++ (PEG: different order)
                        b.sequence(RustKeyword.DYNAMIC_CAST, typeIdEnclosed, "(", expression, ")"), // C++
                        b.sequence(RustKeyword.STATIC_CAST, typeIdEnclosed, "(", expression, ")"), // C++
                        b.sequence(RustKeyword.REINTERPRET_CAST, typeIdEnclosed, "(", expression, ")"), // C++
                        b.sequence(RustKeyword.CONST_CAST, typeIdEnclosed, "(", expression, ")"), //C++
                        b.sequence(
                                RustKeyword.TYPEID, "(",
                                b.firstOf(
                                        expression, // C++
                                        typeId // C++
                                ),
                                ")"
                        )
                ),
                b.zeroOrMore(
                        b.firstOf(
                                b.sequence("[", exprOrBracedInitList, "]"),
                                b.sequence("(", b.optional(expressionList), ")"),
                                b.sequence(
                                        b.firstOf(
                                                ".",
                                                "->"
                                        ),
                                        b.firstOf(
                                                b.sequence(b.optional(RustKeyword.TEMPLATE), idExpression),
                                                pseudoDestructorName
                                        )
                                ),
                                "++",
                                "--"
                        )
                )
        ).skipIfOneChild();

        b.rule(cudaKernel).is(
                b.sequence("<<", "<", b.optional(expressionList), ">>", ">") // CUDA
        );

        b.rule(typeIdEnclosed).is( // todo
                "<",
                b.firstOf(
                        b.sequence(typeId, ">"),
                        b.sequence(innerTypeId, ">>")
                )
        );

        b.rule(expressionList).is(
                initializerList // C++
        );

        b.rule(pseudoDestructorName).is(
                b.firstOf(
                        b.sequence(b.optional(nestedNameSpecifier), typeName, "::", "~", typeName), // C++
                        b.sequence(nestedNameSpecifier, RustKeyword.TEMPLATE, simpleTemplateId, "::", "~", typeName), // C++
                        b.sequence("~", typeName), // C++
                        b.sequence("~", decltypeSpecifier) // C++
                )
        );

        b.rule(unaryExpression).is(
                b.firstOf(
                        b.sequence(unaryOperator, castExpression), // C++ (PEG: different order)
                        postfixExpression, // C++
                        b.sequence("++", castExpression), // C++
                        b.sequence("--", castExpression), // C++
                        b.sequence(
                                RustKeyword.SIZEOF,
                                b.firstOf(
                                        unaryExpression, // C++
                                        b.sequence("(", typeId, ")"), // C++
                                        b.sequence("...", "(", IDENTIFIER, ")") // C++
                                )
                        ),
                        b.sequence(RustKeyword.ALIGNOF, "(", typeId, ")"), // C++
                        noexceptExpression, // C++
                        newExpression, // C++
                        deleteExpression // C++
                )
        ).skipIfOneChild();

        b.rule(unaryOperator).is(
                b.firstOf("*", "&", "+", "-", "!", RustKeyword.NOT, "~", RustKeyword.COMPL) // C++
        );

        b.rule(newExpression).is( // todo gcnew must be string
                b.sequence(
                        b.optional("::"),
                        b.firstOf(
                                RustKeyword.NEW,
                                RustKeyword.GCNEW
                        ),
                        b.firstOf(
                                b.sequence("(", typeId, ")", b.optional(newInitializer)),
                                b.sequence(
                                        b.optional(newPlacement),
                                        b.firstOf(
                                                b.sequence(newTypeId, b.optional(newInitializer)), // C++
                                                b.sequence("(", typeId, ")", b.optional(newInitializer)) // C++
                                        )
                                )
                        )
                )
        );

        b.rule(newPlacement).is(
                "(", expressionList, ")" // C++
        );

        b.rule(newTypeId).is(
                typeSpecifierSeq, b.optional(newDeclarator) // C++
        );

        b.rule(newDeclarator).is(
                b.firstOf(
                        b.sequence(ptrOperator, b.optional(newDeclarator)), // C++
                        noptrNewDeclarator // C++
                )
        );

        b.rule(noptrNewDeclarator).is(
                "[", expression, "]", b.optional(attributeSpecifierSeq), b.zeroOrMore("[", constantExpression, "]", b.optional(
                        attributeSpecifierSeq)) // C++
        );

        b.rule(newInitializer).is(
                b.firstOf(
                        b.sequence("(", b.optional(expressionList), ")"), // C++
                        bracedInitList // C++
                )
        );

        b.rule(deleteExpression).is(
                b.optional("::"), RustKeyword.DELETE, b.optional("[", "]"), castExpression // C++
        );

        b.rule(noexceptExpression).is(
                RustKeyword.NOEXCEPT, "(", expression, ")" // C++
        );

        b.rule(castExpression).is(
                b.firstOf(
                        // bracedInitList: C-COMPATIBILITY: C99 compound literals
                        b.sequence("(", typeId, ")", b.firstOf(castExpression, bracedInitList)), // C++
                        unaryExpression // C++ (PEG: different order)
                )
        ).skipIfOneChild();

        b.rule(pmExpression).is(
                castExpression, b.zeroOrMore(b.firstOf(".*", "->*"), castExpression) // C++
        ).skipIfOneChild();

        b.rule(multiplicativeExpression).is(
                pmExpression, b.zeroOrMore(b.firstOf("*", "/", "%"), pmExpression) // C++
        ).skipIfOneChild();

        b.rule(additiveExpression).is(
                multiplicativeExpression, b.zeroOrMore(b.firstOf("+", "-"), multiplicativeExpression) // C++
        ).skipIfOneChild();

        b.rule(shiftExpression).is(
                additiveExpression, b.zeroOrMore(b.firstOf("<<", ">>"), additiveExpression) // C++
        ).skipIfOneChild();

        b.rule(relationalExpression).is(
                shiftExpression, b.zeroOrMore(b.firstOf("<", ">", "<=", ">="), shiftExpression) // C++
        ).skipIfOneChild();

        b.rule(equalityExpression).is(
                relationalExpression, b.zeroOrMore(b.firstOf("==", "!=", RustKeyword.NOT_EQ), relationalExpression) // C++
        ).skipIfOneChild();

        b.rule(andExpression).is(
                equalityExpression, b.zeroOrMore(b.firstOf("&", RustKeyword.BITAND), equalityExpression) // C++
        ).skipIfOneChild();

        b.rule(exclusiveOrExpression).is(
                andExpression, b.zeroOrMore(b.firstOf("^", RustKeyword.XOR), andExpression) // C++
        ).skipIfOneChild();

        b.rule(inclusiveOrExpression).is(
                exclusiveOrExpression, b.zeroOrMore(b.firstOf("|", RustKeyword.BITOR), exclusiveOrExpression) // C++
        ).skipIfOneChild();

        b.rule(logicalAndExpression).is(
                inclusiveOrExpression, b.zeroOrMore(b.firstOf("&&", RustKeyword.AND), inclusiveOrExpression) // C++
        ).skipIfOneChild();

        b.rule(logicalOrExpression).is(
                logicalAndExpression, b.zeroOrMore(b.firstOf("||", RustKeyword.OR), logicalAndExpression) // C++
        ).skipIfOneChild();

        b.rule(conditionalExpression).is(
                // EXTENSION: gcc's conditional with omitted operands: the expression is optional
                logicalOrExpression, b.optional("?", b.optional(expression), ":", assignmentExpression) // C++
        ).skipIfOneChild();

        b.rule(throwExpression).is(
                RustKeyword.THROW, b.optional(assignmentExpression) // C++
        );

        b.rule(assignmentExpression).is(
                b.firstOf(
                        b.sequence(logicalOrExpression, assignmentOperator, initializerClause), // C++ (PEG: different order)
                        conditionalExpression, // C++ (PEG: different order)
                        throwExpression // C++
                )
        ).skipIfOneChild();

        b.rule(assignmentOperator).is(
                b.firstOf("=", "*=", "/=", "%=", "+=", "-=", ">>=", "<<=", "&=", RustKeyword.AND_EQ, "^=", RustKeyword.XOR_EQ, "|=",
                        RustKeyword.OR_EQ) // C++
        );

        b.rule(expression).is(
                assignmentExpression, b.zeroOrMore(",", assignmentExpression) // C++
        );

        b.rule(constantExpression).is(
                conditionalExpression // C++
        );
    }

    // A.5 Statements
    //
    private static void statements(LexerfulGrammarBuilder b) {

        b.rule(statement).is(
                b.firstOf(
                        labeledStatement, // C++
                        b.sequence(b.optional(attributeSpecifierSeq), expressionStatement), // C++
                        b.sequence(b.optional(attributeSpecifierSeq), compoundStatement), // C++
                        b.sequence(b.optional(attributeSpecifierSeq), selectionStatement), // C++
                        b.sequence(b.optional(attributeSpecifierSeq), iterationStatement), // C++
                        b.sequence(b.optional(attributeSpecifierSeq), jumpStatement), // C++
                        declarationStatement, // C++
                        b.sequence(b.optional(attributeSpecifierSeq), tryBlock) // C++
                )
        );

        b.rule(emptyStatement).is(
                ";" // todo: not C++
        );

        b.rule(initStatement).is(
                b.firstOf(
                        expressionStatement, // C++
                        simpleDeclaration // C++
                )
        );

        b.rule(condition).is(
                b.firstOf(
                        b.sequence(b.optional(attributeSpecifierSeq), conditionDeclSpecifierSeq, declarator, braceOrEqualInitializer), // C++
                        expression // C++ (PEG: different order)
                )
        );

        b.rule(labeledStatement).is(
                b.firstOf(
                        b.sequence(b.optional(attributeSpecifierSeq), IDENTIFIER, ":", statement), // C++
                        b.sequence(
                                b.optional(attributeSpecifierSeq), RustKeyword.CASE, constantExpression,
                                b.firstOf(
                                        b.sequence(":", statement), // C++
                                        b.sequence("...", constantExpression, ":", statement) // EXTENSION: gcc's case range
                                )
                        ),
                        b.sequence(b.optional(attributeSpecifierSeq), RustKeyword.DEFAULT, ":", statement) // C++
                )
        );

        b.rule(expressionStatement).is(
                b.optional(expression), ";" // C++
        );

        b.rule(compoundStatement).is(
                "{", b.optional(statementSeq), "}" // C++
        );

        b.rule(statementSeq).is(
                b.oneOrMore(statement) // C++
        );

        b.rule(selectionStatement).is(
                b.firstOf(
                        b.sequence(RustKeyword.IF, b.optional(RustKeyword.CONSTEXPR), "(", b.optional(initStatement), condition, ")",
                                statement, b.optional(RustKeyword.ELSE, statement)), // C++
                        b.sequence(RustKeyword.SWITCH, "(", b.optional(initStatement), condition, ")", statement)
                )
        );

        b.rule(conditionDeclSpecifierSeq).is( // todo decl-specifier-seq
                b.oneOrMore(
                        b.nextNot(declarator, b.firstOf("=", "{")),
                        declSpecifier
                ),
                b.optional(attributeSpecifierSeq)
        ).skipIfOneChild();

        b.rule(iterationStatement).is(
                b.firstOf(
                        b.sequence(RustKeyword.WHILE, "(", condition, ")", statement), // C++
                        b.sequence(RustKeyword.DO, statement, RustKeyword.WHILE, "(", expression, ")", ";"), // C++
                        b.sequence(RustKeyword.FOR, "(", initStatement, b.optional(condition), ";", b.optional(expression), ")",
                                statement), // C++
                        b.sequence(RustKeyword.FOR, "(", forRangeDeclaration, ":", forRangeInitializer, ")", statement), // C++
                        b.sequence(RustKeyword.FOR, "each", "(", forRangeDeclaration, "in", forRangeInitializer, ")", statement) // C++/CLI
                )
        );

        b.rule(forRangeDeclaration).is( // todo
                b.firstOf(
                        b.sequence(b.optional(attributeSpecifierSeq), forRangeDeclSpecifierSeq, declarator), // C++
                        b.sequence(b.optional(attributeSpecifierSeq), declSpecifierSeq, b.optional(refQualifier), "[", identifierList,
                                "]") // C++
                )
        );

        b.rule(forRangeDeclSpecifierSeq).is( // todo decl-specifier-seq
                b.oneOrMore(
                        b.nextNot(b.optional(declarator), b.firstOf(":", "in")),
                        declSpecifier,
                        b.optional(attributeSpecifierSeq)
                )
        ).skipIfOneChild();

        b.rule(forRangeInitializer).is(
                exprOrBracedInitList // C++
        );

        b.rule(jumpStatement).is(
                b.firstOf(
                        b.sequence(RustKeyword.BREAK, ";"), // C++
                        b.sequence(RustKeyword.CONTINUE, ";"), // C++
                        b.sequence(RustKeyword.RETURN, b.optional(exprOrBracedInitList), ";"), // C++
                        b.sequence(RustKeyword.GOTO, IDENTIFIER, ";") // C++
                )
        );

        b.rule(declarationStatement).is(
                blockDeclaration // C++
        );
    }

    // A.6 Declarations
    //
    private static void declarations(LexerfulGrammarBuilder b) {
        b.rule(declarationSeq).is(
                b.oneOrMore(declaration) // C++
        ).skipIfOneChild();

        b.rule(declaration).is(
                b.firstOf(
                        blockDeclaration, // C++
                        nodeclspecFunctionDeclaration, // C++
                        functionDefinition, // C++
                        templateDeclaration, // C++
                        deductionGuide, // C++
                        cliGenericDeclaration, // C++/CLI
                        explicitInstantiation, // C++
                        explicitSpecialization, // C++
                        linkageSpecification, // C++
                        namespaceDefinition, // C++
                        emptyDeclaration, // C++
                        attributeDeclaration, // C++
                        vcAtlDeclaration // Attributted-ATL
                )
        );

        b.rule(blockDeclaration).is(
                b.firstOf(
                        simpleDeclaration, // C++
                        asmDefinition, // C++
                        namespaceAliasDefinition, // C++
                        usingDeclaration, // C++
                        usingDirective, // C++
                        staticAssertDeclaration, // C++
                        aliasDeclaration, // C++
                        opaqueEnumDeclaration // C++
                )
        ).skip();

        b.rule(nodeclspecFunctionDeclaration).is(
                b.optional(attributeSpecifierSeq), declarator, ";" // C++
        );

        b.rule(aliasDeclaration).is(
                RustKeyword.USING, IDENTIFIER, b.optional(attributeSpecifierSeq), "=", definingTypeId, ";" // C++
        );

        b.rule(simpleDeclaration).is(
                b.firstOf(
                        b.sequence(b.optional(declSpecifierSeq), b.optional(initDeclaratorList), ";"), // C++ // todo wrong
                        b.sequence(b.optional(cliAttributes), b.optional(attributeSpecifierSeq), b.optional(declSpecifierSeq), b
                                .optional(initDeclaratorList), ";"), // C++ // todo wrong
                        b.sequence(b.optional(attributeSpecifierSeq), declSpecifierSeq, b.optional(refQualifier), "[", identifierList,
                                "]", initializer, ";") // C++ // todo wrong
                )
        );

        b.rule(staticAssertDeclaration).is(
                RustKeyword.STATIC_ASSERT, "(", constantExpression, b.optional(",", STRING), ")", ";" // C++
        );

        b.rule(emptyDeclaration).is(
                ";" // C++
        );

        b.rule(attributeDeclaration).is(
                attributeSpecifierSeq, ";" // C++
        );

        b.rule(declSpecifier).is(
                b.firstOf(
                        storageClassSpecifier, // C++
                        definingTypeSpecifier, // C++
                        functionSpecifier, // C++
                        RustKeyword.FRIEND, // C++
                        RustKeyword.TYPEDEF, // C++
                        RustKeyword.CONSTEXPR, // C++
                        RustKeyword.INLINE // C++
                )
        );

        b.rule(declSpecifierSeq).is( // todo
                b.oneOrMore(
                        b.nextNot(b.optional(initDeclaratorList), ";"), declSpecifier, b.optional(attributeSpecifierSeq) // C++
                        // decl-specifier decl-specifier-seq // todo missing
                )
        ).skipIfOneChild();

        b.rule(storageClassSpecifier).is(
                b.firstOf(
                        RustKeyword.REGISTER, // C++11, C++14, deprecated with C++17
                        RustKeyword.STATIC, // C++
                        RustKeyword.THREAD_LOCAL, // C++
                        RustKeyword.EXTERN, // C++
                        RustKeyword.MUTABLE // C++
                )
        );

        b.rule(functionSpecifier).is(
                b.firstOf(
                        RustKeyword.VIRTUAL, // C++
                        RustKeyword.EXPLICIT // C++
                )
        );

        b.rule(typedefName).is(
                IDENTIFIER // C++
        );

        b.rule(typeSpecifier).is( // todo wrong
                b.firstOf(
                        classSpecifier, // C++
                        enumSpecifier, // C++
                        trailingTypeSpecifier // C++ (PEG: different order)

                        // simple-type-specifier
                        // elaborated-type-specifier
                        // typename-specifier
                        // cv-qualifier
                )
        ).skip();

        b.rule(typeSpecifierSeq).is(
                b.oneOrMore(
                        typeSpecifier, b.optional(attributeSpecifierSeq) // C++
                )
        ).skipIfOneChild();

        b.rule(definingTypeSpecifier).is(
                b.firstOf(
                        typeSpecifier, // C++
                        classSpecifier, // C++
                        enumSpecifier // C++
                )
        ).skip();

        b.rule(definingTypeSpecifierSeq).is(
                b.oneOrMore(definingTypeSpecifier, b.optional(attributeSpecifierSeq)) // C++
        ).skipIfOneChild();

        b.rule(trailingTypeSpecifier).is( // todo wrong
                b.firstOf(
                        simpleTypeSpecifier, // C++
                        elaboratedTypeSpecifier, // C++
                        typenameSpecifier, // C++
                        cvQualifier, // C++
                        cliDelegateSpecifier // C++/CLI
                )
        ).skip();

        b.rule(simpleTypeSpecifier).is(
                b.firstOf(
                        b.sequence(b.optional(nestedNameSpecifier), typeName), // C++
                        b.sequence(nestedNameSpecifier, RustKeyword.TEMPLATE, simpleTemplateId), // C++
                        b.sequence(b.optional(nestedNameSpecifier), templateName), // C++
                        RustKeyword.CHAR, // C++
                        RustKeyword.CHAR16_T, // C++
                        RustKeyword.CHAR32_T, // C++
                        RustKeyword.WCHAR_T, // C++
                        RustKeyword.BOOL, // C++
                        RustKeyword.SHORT, // C++
                        RustKeyword.INT, // C++
                        RustKeyword.LONG, // C++
                        RustKeyword.SIGNED, // C++
                        RustKeyword.UNSIGNED, // C++
                        RustKeyword.FLOAT, // C++
                        RustKeyword.DOUBLE, // C++
                        RustKeyword.VOID, // C++
                        RustKeyword.AUTO, // C++
                        decltypeSpecifier // C++
                )
        ).skipIfOneChild();

        b.rule(typeName).is(
                b.firstOf(
                        className, // C++
                        enumName, // C++
                        typedefName, // C++
                        simpleTemplateId // C++
                )
        );

        b.rule(decltypeSpecifier).is(
                RustKeyword.DECLTYPE, "(",
                b.firstOf(
                        b.sequence(expression, ")"), // C++
                        b.sequence(RustKeyword.AUTO, ")") // C++
                )
        );

        b.rule(elaboratedTypeSpecifier).is( // todo
                b.optional(cliAttributes),
                b.firstOf( // PEG: different order
                        b.sequence(
                                classKey,
                                b.firstOf(
                                        templateId, // C++
                                        b.sequence(nestedNameSpecifier, b.optional(RustKeyword.TEMPLATE), simpleTemplateId), // C++
                                        b.sequence(b.optional(attributeSpecifierSeq), b.optional(nestedNameSpecifier), IDENTIFIER) // C++
                                )
                        ),
                        b.sequence(RustKeyword.ENUM, b.optional(nestedNameSpecifier), IDENTIFIER) // C++
                )
        );

        b.rule(enumName).is(
                IDENTIFIER // C++
        );

        b.rule(enumSpecifier).is(
                b.firstOf(
                        b.sequence(enumHead, "{", b.optional(enumeratorList), "}"), // C++
                        b.sequence(enumHead, "{", enumeratorList, ",", "}") // C++
                )
        );

        b.rule(enumHead).is(
                b.optional(vcAtlAttribute), b.optional(cliTopLevelVisibility), enumKey, b.optional(attributeSpecifierSeq), b
                        .optional(enumHeadName), b.optional(enumBase) // C++
        );

        b.rule(enumHeadName).is(
                b.optional(nestedNameSpecifier), IDENTIFIER // C++
        );

        b.rule(opaqueEnumDeclaration).is(
                enumKey, b.optional(attributeSpecifierSeq), b.optional(nestedNameSpecifier), IDENTIFIER, b.optional(enumBase), ";" // C++
        );

        b.rule(enumKey).is(
                RustKeyword.ENUM, b.optional(b.firstOf(RustKeyword.CLASS, RustKeyword.STRUCT)) // C++
        );

        b.rule(enumBase).is(
                ":", typeSpecifierSeq // C++
        );

        b.rule(enumeratorList).is(
                enumeratorDefinition, b.zeroOrMore(",", enumeratorDefinition) // C++
        );

        b.rule(enumeratorDefinition).is(
                enumerator, b.optional("=", constantExpression) // C++
        );

        b.rule(enumerator).is(
                IDENTIFIER, b.optional(attributeSpecifierSeq) // C++
        );

        b.rule(namespaceName).is(
                b.firstOf(
                        IDENTIFIER, // C++
                        namespaceAlias // C++
                )
        );

        b.rule(namespaceDefinition).is(
                b.firstOf(
                        namedNamespaceDefinition, // C++
                        unnamedNamespaceDefinition, // C++
                        nestedNamespaceDefinition // C++
                )
        );

        b.rule(namedNamespaceDefinition).is(
                b.optional(RustKeyword.INLINE), RustKeyword.NAMESPACE, b.optional(attributeSpecifierSeq), IDENTIFIER, "{",
                namespaceBody, "}" // C++
        );

        b.rule(unnamedNamespaceDefinition).is(
                b.optional(RustKeyword.INLINE), RustKeyword.NAMESPACE, b.optional(attributeSpecifierSeq), "{", namespaceBody, "}" // C++
        );

        b.rule(nestedNamespaceDefinition).is(
                RustKeyword.NAMESPACE, enclosingNamespaceSpecifier, "::", IDENTIFIER, "{", namespaceBody, "}" // C++
        );

        b.rule(enclosingNamespaceSpecifier).is(
                IDENTIFIER, b.zeroOrMore("::", IDENTIFIER, b.nextNot("{")) // C++
        );

        b.rule(namespaceBody).is(
                b.optional(declarationSeq) // C++
        );

        b.rule(namespaceAlias).is(
                IDENTIFIER // C++
        );

        b.rule(namespaceAliasDefinition).is(
                RustKeyword.NAMESPACE, IDENTIFIER, "=", qualifiedNamespaceSpecifier, ";" // C++
        );

        b.rule(qualifiedNamespaceSpecifier).is(
                b.optional(nestedNameSpecifier), namespaceName // C++
        );

        b.rule(usingDeclaration).is(
                RustKeyword.USING, usingDeclaratorList, ";" // C++
        );

        b.rule(usingDeclaratorList).is(
                usingDeclarator, b.optional("..."), b.zeroOrMore(",", usingDeclarator, b.optional("...")) // C++
        );

        b.rule(usingDeclarator).is(
                b.optional(RustKeyword.TYPENAME), nestedNameSpecifier, unqualifiedId // C++
        );

        b.rule(usingDirective).is(
                b.optional(attributeSpecifierSeq), RustKeyword.USING, RustKeyword.NAMESPACE, b.optional(nestedNameSpecifier),
                namespaceName, ";" // C++
        );

        b.rule(asmDefinition).is(
                b.firstOf(
                        b.sequence(
                                b.firstOf(RustKeyword.ASM, "__asm__"), // C++ asm; GCC: __asm__
                                b.optional(b.firstOf(RustKeyword.VIRTUAL, RustKeyword.INLINE, "__virtual__")), // GCC asm qualifiers
                                "(", STRING, ")", ";"
                        ),
                        b.sequence(b.firstOf("__asm", RustKeyword.ASM), b.firstOf( // VS
                                b.sequence("{", b.oneOrMore(b.nextNot(b.firstOf("}", EOF)), b.anyToken()), "}", b.optional(";")), // VS __asm block
                                b.sequence(b.oneOrMore(b.nextNot(b.firstOf(";", EOF)), b.anyToken()), ";") // VS __asm ... ;
                                )
                        )
                )
        );

        b.rule(asmLabel).is(
                b.firstOf(RustKeyword.ASM, "__asm__"), "(", STRING, ")" // GCC ASM label
        );

        b.rule(linkageSpecification).is(
                RustKeyword.EXTERN, STRING,
                b.firstOf(
                        b.sequence("{", b.optional(declarationSeq), "}"), // C++
                        declaration // C++
                )
        );

        b.rule(attributeSpecifierSeq).is(
                b.oneOrMore(attributeSpecifier) // C++
        ).skipIfOneChild();

        b.rule(attributeSpecifier).is(
                b.firstOf(
                        b.sequence("[", "[", b.optional(attributeUsingPrefix), attributeList, "]", "]"), // C++
                        alignmentSpecifier // C++
                ));

        b.rule(alignmentSpecifier).is(
                b.firstOf(
                        b.sequence(RustKeyword.ALIGNAS, "(", typeId, b.optional("..."), ")"), // C++
                        b.sequence(RustKeyword.ALIGNAS, "(", constantExpression, b.optional("..."), ")"), // C++
                        b.sequence(attributeUsingPrefix, ":"), // C++
                        b.sequence(RustKeyword.USING, attributeNamespace, ":") // C++
                ));

        b.rule(attributeUsingPrefix).is(
                RustKeyword.USING, attributeNamespace, ":" // C++
        );

        b.rule(attributeList).is(
                b.optional(attribute, b.optional("...")), // C++
                b.zeroOrMore(
                        ",", b.optional(attribute, b.optional("...")) // C++
                )
        );

        b.rule(attribute).is(
                attributeToken, b.optional(attributeArgumentClause) // C++
        );

        b.rule(attributeToken).is(
                b.firstOf(
                        b.sequence(IDENTIFIER, b.nextNot("::")), // C++
                        attributeScopedToken // C++
                )
        );

        b.rule(attributeScopedToken).is(
                attributeNamespace, "::", IDENTIFIER // C++
        );

        b.rule(attributeNamespace).is(
                IDENTIFIER // C++
        );

        b.rule(attributeArgumentClause).is(
                "(", b.optional(balancedTokenSeq), ")" // C++
        );

        b.rule(balancedTokenSeq).is(
                b.oneOrMore(balancedToken) // C++
        ).skipIfOneChild();

        b.rule(balancedToken).is(
                b.firstOf(
                        b.sequence("(", b.optional(balancedTokenSeq), ")"), // C++
                        b.sequence("{", b.optional(balancedTokenSeq), "}"), // C++
                        b.sequence("[", b.optional(balancedTokenSeq), "]"), // C++
                        b.oneOrMore(b.nextNot(b.firstOf("(", ")", "{", "}", "[", "]", EOF)), b.anyToken()) // C++
                )
        );
    }

    // A.7 Declarators
    //
    private static void declarators(LexerfulGrammarBuilder b) {
        b.rule(initDeclaratorList).is(
                initDeclarator, b.zeroOrMore(",", initDeclarator) // C++
        );

        b.rule(initDeclarator).is(
                declarator, b.optional(asmLabel), b.optional(initializer) // C++ (asmLabel: GCC ASM label)
        );

        b.rule(declarator).is(
                b.firstOf(
                        ptrDeclarator, // C++
                        b.sequence(noptrDeclarator, parametersAndQualifiers, trailingReturnType) // C++
                )
        );

        b.rule(ptrDeclarator).is(
                b.firstOf(
                        b.sequence(ptrOperator, ptrDeclarator), // C++
                        noptrDeclarator // C++ (PEG: different order)
                )
        ).skipIfOneChild();

        b.rule(noptrDeclarator).is(
                b.firstOf(
                        b.sequence(declaratorId, b.optional(attributeSpecifierSeq)), // C++
                        b.sequence("(", ptrDeclarator, ")") // C++
                ),
                b.zeroOrMore(
                        b.firstOf(
                                parametersAndQualifiers,
                                b.sequence("[", b.optional(constantExpression), "]", b.optional(attributeSpecifierSeq))
                        )
                )
        ).skipIfOneChild();

        b.rule(parametersAndQualifiers).is(
                "(", parameterDeclarationClause, ")", // C++
                b.optional(attributeSpecifierSeq), // C++ todo wrong position
                b.optional(cvQualifierSeq), // C++
                b.optional(cliFunctionModifiers), // C++/CLI
                b.optional(refQualifier), // C++
                b.optional(noexceptSpecifier), // C++
                b.optional(trailingReturnType) // todo wrong?
        );

        b.rule(trailingReturnType).is(
                "->", typeId // C++
        );

        b.rule(ptrOperator).is(
                b.firstOf(
                        b.sequence("*", b.optional(attributeSpecifierSeq), b.optional(cvQualifierSeq)), // C++
                        b.sequence("&", b.optional(attributeSpecifierSeq)), // C++
                        b.sequence("&&", b.optional(attributeSpecifierSeq)), // C++
                        b.sequence(nestedNameSpecifier, "*", b.optional(attributeSpecifierSeq), b.optional(cvQualifierSeq)), //C++
                        //C++/CLI handle reference
                        b.sequence("^", b.optional(attributeSpecifierSeq), b.optional(cvQualifierSeq)), // C++/CLI
                        b.sequence("%", b.optional(attributeSpecifierSeq)), // C++/CLI
                        // C++/CLI tracking reference
                        b.sequence("^%", b.optional(attributeSpecifierSeq)) // C++/CLI
                )
        );

        b.rule(cvQualifierSeq).is(
                b.oneOrMore(cvQualifier) // C++
        ).skipIfOneChild();

        b.rule(cvQualifier).is(
                b.firstOf(RustKeyword.CONST, RustKeyword.VOLATILE) // C++
        );

        b.rule(refQualifier).is(
                b.firstOf("&", "&&") // C++
        );

        b.rule(declaratorId).is(
                b.firstOf(
                        b.sequence(b.optional(nestedNameSpecifier), className), // todo wrong?
                        b.sequence(b.optional("..."), idExpression) // C++
                )
        );

        b.rule(typeId).is(
                typeSpecifierSeq, b.optional(abstractDeclarator) // C++
        ).skip();

        b.rule(definingTypeId).is(
                definingTypeSpecifierSeq, b.optional(abstractDeclarator) // C++
        );

        b.rule(abstractDeclarator).is(
                b.firstOf(
                        ptrAbstractDeclarator, // C++
                        b.sequence(b.optional(noptrAbstractDeclarator), parametersAndQualifiers, trailingReturnType), // C++
                        abstractPackDeclarator // C++
                )
        );

        b.rule(ptrAbstractDeclarator).is(
                b.oneOrMore(
                        b.firstOf(noptrAbstractDeclarator, ptrOperator) // C++ //todo wrong?
                )
        );

        b.rule(noptrAbstractDeclarator).is(
                b.oneOrMore(
                        b.firstOf(
                                parametersAndQualifiers, // C++
                                b.sequence("[", b.optional(constantExpression), "]", b.optional(attributeSpecifierSeq)), // C++
                                b.sequence("(", ptrAbstractDeclarator, ")") // C++
                        )
                )
        );

        b.rule(abstractPackDeclarator).is(
                b.firstOf(
                        noptrAbstractPackDeclarator, // C++
                        b.sequence(ptrOperator, abstractPackDeclarator) // C++
                )
        );

        b.rule(noptrAbstractPackDeclarator).is(
                b.oneOrMore(
                        b.firstOf(
                                parametersAndQualifiers, // C++
                                b.sequence("[", b.optional(constantExpression), "]", b.optional(attributeSpecifierSeq)), // C++
                                "..." // C++
                        )
                )
        );

        b.rule(parameterDeclarationClause).is(
                b.firstOf(
                        b.sequence(parameterDeclarationList, ",", "..."), // C++
                        b.sequence(b.optional(parameterDeclarationList), b.optional("...")), // C++ (PEG: different order)
                        cliParameterArray // C++/CLI
                )
        );

        b.rule(cliParameterArray).is(
                b.optional(attribute), "...", parameterDeclaration
        );

        b.rule(parameterDeclarationList).is(
                parameterDeclaration, b.zeroOrMore(",", parameterDeclaration) // C++
        );

        b.rule(parameterDeclaration).is( //todo wrong
                b.firstOf(
                        b.sequence(b.optional(attributeSpecifierSeq), b.optional(vcAtlAttribute), parameterDeclSpecifierSeq, declarator,
                                b.optional("=", initializerClause)), // C++
                        b.sequence(b.optional(attributeSpecifierSeq), parameterDeclSpecifierSeq, b.optional(abstractDeclarator), b
                                .optional("=", initializerClause))) // C++
        );

        b.rule(parameterDeclSpecifierSeq).is( // todo is decl-specifier-seq
                b.zeroOrMore(
                        b.nextNot(b.optional(declarator), b.firstOf("=", ")", ",")), declSpecifier, b.optional("...")
                ),
                b.optional(attributeSpecifierSeq)
        ).skipIfOneChild();

        b.rule(functionDefinition).is(
                b.optional(attributeSpecifierSeq), // C++
                b.optional(cliAttributes), // C++/CLI
                b.optional(functionDeclSpecifierSeq), // todo is decl-specifier-seq
                declarator, //C++
                b.optional(virtSpecifierSeq), // C++
                functionBody // C++
        );

        b.rule(functionDeclSpecifierSeq).is( // todo is decl-specifier-seq
                b.oneOrMore(
                        b.nextNot(declarator, b.optional(virtSpecifierSeq), functionBody), declSpecifier
                ),
                b.optional(attributeSpecifierSeq)
        ).skipIfOneChild();

        b.rule(functionBody).is(
                b.firstOf(
                        b.sequence(b.optional(ctorInitializer), compoundStatement), // C++
                        functionTryBlock, // C++
                        b.sequence("=", RustKeyword.DELETE, ";"), // C++
                        b.sequence("=", RustKeyword.DEFAULT, ";") // C++
                )
        );

        b.rule(initializer).is(
                b.firstOf(
                        braceOrEqualInitializer, // C++
                        b.sequence("(", expressionList, ")") // C++
                )
        );

        b.rule(braceOrEqualInitializer).is(
                b.firstOf(
                        b.sequence("=", initializerClause), // C++
                        bracedInitList // C++
                )
        ).skip();

        b.rule(initializerClause).is(
                // C-COMPATIBILITY: C99 designated initializers
                b.optional(
                        b.firstOf(
                                b.sequence(b.zeroOrMore("[", constantExpression, "]"), b.zeroOrMore(".", IDENTIFIER), "="), // C99
                                b.sequence("[", constantExpression, "...", constantExpression, "]", "=") // EXTENSION: gcc's designated initializers range
                        )
                ),
                b.firstOf(
                        assignmentExpression, // C++
                        bracedInitList // C++
                )
        ).skipIfOneChild();

        b.rule(initializerList).is(
                initializerClause, b.optional("..."), b.zeroOrMore(",", initializerClause, b.optional("...")) // C++
        );

        b.rule(bracedInitList).is(
                "{",
                b.firstOf(
                        b.sequence(LITERAL, b.oneOrMore(",", LITERAL), "}"), // syntax sugar: speed-up initialisation of big arrays
                        b.sequence(initializerList, b.optional(","), "}"), // C++
                        "}" // C++
                )
        );

        b.rule(exprOrBracedInitList).is(
                b.firstOf(
                        expression,
                        bracedInitList
                )
        ).skip();

    }

    // A.8 Classes
    //
    private static void classes(LexerfulGrammarBuilder b) {
        b.rule(className).is(
                b.firstOf(
                        simpleTemplateId, // C++
                        IDENTIFIER // C++ (PEG: different order)
                )
        );

        b.rule(classSpecifier).is(
                b.optional(vcAtlAttribute), classHead, "{", b.optional(memberSpecification), "}" // C++
        );

        b.rule(classHead).is(
                b.optional(cliTopLevelVisibility), b.optional(vcAtlAttribute), classKey, b.optional(attributeSpecifierSeq),
                b.firstOf(
                        b.sequence(classHeadName, b.optional(classVirtSpecifier), b.optional(baseClause), b.optional(
                                attributeSpecifierSeq)), // C++ // todo wrong attributeSpecifierSeq
                        b.optional(baseClause) // C++
                )
        );

        b.rule(classHeadName).is(
                b.optional(nestedNameSpecifier), className // C++
        );

        b.rule(cliTopLevelVisibility).is(
                b.firstOf(
                        RustKeyword.PUB,
                        RustKeyword.PRIVATE
                )
        );

        b.rule(classVirtSpecifier).is(
                b.firstOf(
                        "final", // C++
                        "sealed", // C++/CLI
                        "abstract" // C++/CLI
                )
        );

        b.rule(classKey).is(
                b.firstOf(b.sequence(b.optional(b.firstOf("ref", "value", "interface")), RustKeyword.CLASS), // C++, C++/CLI
                        b.sequence(b.optional(b.firstOf("ref", "value", "interface")), RustKeyword.STRUCT), // C++, C++/CLI
                        RustKeyword.UNION // C++, C++/CLI
                )
        );

        b.rule(memberSpecification).is(
                b.oneOrMore(
                        b.firstOf(
                                memberDeclaration, // C++
                                b.sequence(accessSpecifier, ":") // C++
                        )
                )
        );

        b.rule(memberDeclaration).is( // todo
                b.firstOf(
                        b.sequence(
                                functionDefinition,
                                b.optional(emptyStatement)),
                        b.sequence(
                                b.optional(attributeSpecifierSeq),
                                b.optional(b.firstOf(cliAttributes, vcAtlAttribute)),
                                b.optional(b.firstOf("initonly", "literal")),
                                b.optional(memberDeclSpecifierSeq),
                                b.optional(memberDeclaratorList),
                                emptyStatement),
                        b.sequence(
                                b.optional("::"),
                                nestedNameSpecifier,
                                b.optional(RustKeyword.TEMPLATE),
                                unqualifiedId,
                                emptyStatement),
                        usingDeclaration, // C++
                        staticAssertDeclaration, // C++
                        templateDeclaration, // C++
                        deductionGuide, // C++
                        aliasDeclaration, // C++
                        // empty-declaration //todo
                        cliGenericDeclaration,
                        cliDelegateSpecifier,
                        cliEventDefinition,
                        cliPropertyDefinition
                )
        );

        b.rule(cliDelegateDeclSpecifierSeq).is(
                b.oneOrMore(
                        b.nextNot(b.optional(declarator), emptyStatement),
                        declSpecifier
                )
        ).skipIfOneChild();

        b.rule(cliDelegateSpecifier).is(
                b.optional(cliAttributes), b.optional(cliTopLevelVisibility), "delegate", cliDelegateDeclSpecifierSeq, declarator,
                emptyStatement
        );

        b.rule(memberDeclSpecifierSeq).is(
                b.oneOrMore(
                        b.nextNot(b.optional(memberDeclaratorList), emptyStatement), declSpecifier
                ),
                b.optional(attributeSpecifierSeq) // todo wrong position
        ).skipIfOneChild();

        b.rule(memberDeclaratorList).is(
                memberDeclarator, b.zeroOrMore(",", memberDeclarator) // C++
        );

        b.rule(memberDeclarator).is(
                b.firstOf(
                        b.sequence(declarator, braceOrEqualInitializer), // todo braceOrEqualInitializer is optional
                        b.sequence(b.optional(IDENTIFIER), b.optional(attributeSpecifierSeq), ":", constantExpression), // C++
                        b
                                .sequence(declarator, b.optional(virtSpecifierSeq), b.optional(cliFunctionModifiers), b
                                        .optional(pureSpecifier)), // C++
                        declarator // ???
                )
        );

        b.rule(virtSpecifierSeq).is(
                b.oneOrMore(virtSpecifier) // C++
        ).skipIfOneChild();

        b.rule(virtSpecifier).is(
                b.firstOf(
                        "override", // C++
                        "final" // C++
                )
        );

        b.rule(pureSpecifier).is(
                "=", "0" // C++
        );

        b.rule(cliFunctionModifiers).is(
                b.oneOrMore(cliFunctionModifier) // C++/CLI
        );

        b.rule(cliFunctionModifier).is(
                b.firstOf("abstract", RustKeyword.NEW, "sealed") // C++/CLI
        );
    }

    // A.9 Derived classes
    //
    private static void derivedClasses(LexerfulGrammarBuilder b) {
        b.rule(baseClause).is(
                ":", baseSpecifierList // C++
        );

        b.rule(baseSpecifierList).is(
                baseSpecifier, b.optional("..."), b.zeroOrMore(",", baseSpecifier, b.optional("...")) // C++
        );

        b.rule(baseSpecifier).is(
                b.firstOf(
                        b.sequence(b.optional(attributeSpecifierSeq), baseTypeSpecifier), // C++
                        b
                                .sequence(b.optional(attributeSpecifierSeq), RustKeyword.VIRTUAL, b.optional(accessSpecifier),
                                        baseTypeSpecifier), // C++
                        b
                                .sequence(b.optional(attributeSpecifierSeq), accessSpecifier, b.optional(RustKeyword.VIRTUAL),
                                        baseTypeSpecifier) // C++
                )
        );

        b.rule(classOrDecltype).is(
                b.firstOf(
                        b.sequence(b.optional(nestedNameSpecifier), className), // C++
                        decltypeSpecifier // C++
                )
        ).skip();

        b.rule(baseTypeSpecifier).is(
                classOrDecltype // C++
        );

        b.rule(accessSpecifier).is(
                b.firstOf(
                        b.sequence(RustKeyword.PROTECTED, RustKeyword.PUB), // ???
                        b.sequence(RustKeyword.PUB, RustKeyword.PROTECTED), // ???
                        b.sequence(RustKeyword.PROTECTED, RustKeyword.PRIVATE), // ???
                        b.sequence(RustKeyword.PRIVATE, RustKeyword.PROTECTED), // ???
                        RustKeyword.PRIVATE, // C++
                        RustKeyword.PROTECTED, // C++
                        RustKeyword.PUB, // rust
                        "internal" // ???
                )
        );
    }

    // A.10 Special member functions
    //
    private static void specialMemberFunctions(LexerfulGrammarBuilder b) {
        b.rule(conversionFunctionId).is(
                RustKeyword.OPERATOR, conversionTypeId // C++
        );

        b.rule(conversionTypeId).is(
                typeSpecifierSeq, b.optional(conversionDeclarator) // C++
        );

        b.rule(conversionDeclarator).is(
                b.oneOrMore(ptrOperator) // C++
        );

        b.rule(ctorInitializer).is(
                ":", memInitializerList // C++
        );

        b.rule(memInitializerList).is(
                memInitializer, b.optional("..."), b.zeroOrMore(",", memInitializer, b.optional("...")) // C++
        );

        b.rule(memInitializer).is(
                b.firstOf(
                        b.sequence(memInitializerId, "(", b.optional(expressionList), ")"), // C++
                        b.sequence(memInitializerId, bracedInitList) // C++
                )
        );

        b.rule(memInitializerId).is(
                b.firstOf(
                        classOrDecltype, // C++
                        IDENTIFIER // C++
                )
        );
    }

    // A.11 Overloading
    //
    private static void overloading(LexerfulGrammarBuilder b) {
        b.rule(operatorFunctionId).is(
                RustKeyword.OPERATOR, overloadableOperator // C++ //todo wrong
        );

        b.rule(overloadableOperator).is( // C++ todo optimize new/delete?
                b.firstOf(
                        b.sequence(RustKeyword.NEW, "[", "]"),
                        b.sequence(RustKeyword.DELETE, "[", "]"),
                        RustKeyword.NEW, RustKeyword.DELETE,
                        "+", "-", "*", "/", "%", "^", RustKeyword.XOR, "&", RustKeyword.BITAND, "|", RustKeyword.BITOR, "~",
                        RustKeyword.COMPL, "!", RustKeyword.NOT, "=", "<", ">", "+=", "-=", "*=", "/=", "%=", "^=", RustKeyword.XOR_EQ,
                        "&=", RustKeyword.AND_EQ, "|=", RustKeyword.OR_EQ, "<<", ">>", ">>=", "<<=", "==", "!=", RustKeyword.NOT_EQ,
                        "<=", ">=", "&&", RustKeyword.AND, "||", RustKeyword.OR, "++", "--", ",", "->*", "->",
                        b.sequence("(", ")"),
                        b.sequence("[", "]")
                )
                // c++ todo missing optional < template-argument-list > ?
        );

        b.rule(literalOperatorId).is(
                // operator string-literal identifier
                // operator user-defined-string-literal
                RustKeyword.OPERATOR, STRING, b.optional(IDENTIFIER)
        );
    }

    private static void properties(LexerfulGrammarBuilder b) {
        b.rule(cliPropertyOrEventName).is(
                b.firstOf(
                        IDENTIFIER,
                        RustKeyword.DEFAULT
                )
        ).skip();

        b.rule(cliPropertyDeclSpecifierSeq).is(
                b.oneOrMore(
                        b.nextNot(declarator, b.optional(cliPropertyBody), b.optional(";")), typeSpecifier
                )
        ).skipIfOneChild();

        b.rule(cliPropertyDefinition).is(
                b.optional(cliAttributes),
                b.optional(cliPropertyModifiers),
                "property",
                b.firstOf(
                        cliPropertyDeclSpecifierSeq,
                        b.sequence(b.optional(nestedNameSpecifier), b.optional(cliPropertyOrEventName))
                ),
                declarator,
                b.firstOf(
                        emptyStatement,
                        cliPropertyBody)
        );

        b.rule(cliPropertyBody).is(
                b.optional(cliPropertyIndexes), "{", cliAccessorSpecification, "}"
        );

        b.rule(cliPropertyModifiers).is(
                b.oneOrMore(b.firstOf(RustKeyword.VIRTUAL, RustKeyword.STATIC))
        );

        b.rule(cliPropertyIndexes).is(
                "[", cliPropertyIndexParameterList, "]"
        );

        b.rule(cliPropertyIndexParameterList).is(
                typeId, b.zeroOrMore(",", typeId)
        );

        b.rule(cliAccessorSpecification).is(
                b.zeroOrMore(b.optional(accessSpecifier, ":"), cliAccessorDeclaration)
        );

        b.rule(cliAccessorDeclaration).is(
                b.firstOf(
                        functionDefinition,
                        b.sequence(b.optional(attribute), b.optional(declSpecifierSeq), b.optional(memberDeclaratorList), ";")
                )
        );

        b.rule(cliEventDefinition).is(
                b.optional(cliAttributes),
                b.optional(cliEventModifiers),
                "event",
                cliEventType,
                IDENTIFIER,
                b.firstOf(
                        emptyStatement,
                        b.sequence("{", cliAccessorSpecification, "}")
                )
        );

        b.rule(cliEventModifiers).is(
                b.oneOrMore(b.firstOf(RustKeyword.VIRTUAL, RustKeyword.STATIC))
        );

        b.rule(cliEventType).is(
                b.firstOf(
                        b.sequence(b.optional("::"), b.optional(nestedNameSpecifier), typeName, b.optional("^")),
                        b.sequence(b.optional("::"), b.optional(nestedNameSpecifier), RustKeyword.TEMPLATE, templateId, "^")
                )
        );
    }

    // A.12 Templates
    //
    private static void templates(LexerfulGrammarBuilder b) {
        b.rule(templateDeclaration).is(
                RustKeyword.TEMPLATE, templateParameterListEnclosed, declaration // C++
        );

        b.rule(templateParameterListEnclosed).is( // syntax sugar C++
                "<",
                b.firstOf(
                        b.sequence(templateParameterList, ">"), // C++
                        b.sequence(b.zeroOrMore(templateParameter, ","), innerTypeParameter, ">>") // syntax sugar C++
                )
        );

        b.rule(templateParameterList).is(
                templateParameter, b.zeroOrMore(",", templateParameter) // C++
        );

        b.rule(templateParameter).is(
                b.firstOf(
                        typeParameter, // C++
                        parameterDeclaration // C++
                )
        );

        b.rule(typeParameter).is(
                b.firstOf(
                        b.sequence(
                                typeParameterKey,
                                b.firstOf(
                                        b.sequence(nestedNameSpecifier, "type", b.optional("=", initializerClause)), // C++ special case to handle ::type (not part of standard)
                                        b.sequence(b.optional(IDENTIFIER), "=", typeId), // C++
                                        b.sequence(b.optional("..."), b.optional(IDENTIFIER)) // C++ (PEG: different order)
                                )
                        ),
                        b.sequence(
                                RustKeyword.TEMPLATE,
                                templateParameterListEnclosed,
                                typeParameterKey,
                                b.firstOf(
                                        b.sequence(b.optional(IDENTIFIER), "=", idExpression), // C++
                                        b.sequence(b.optional("..."), b.optional(IDENTIFIER)) // C++ (PEG: different order)
                                )
                        )
                )
        );

        b.rule(innerTypeParameter).is(
                b.firstOf(
                        b.sequence(typeParameterKey, b.optional(IDENTIFIER), "=", innerTypeId),
                        b.sequence(RustKeyword.TEMPLATE, templateParameterListEnclosed, RustKeyword.CLASS, b.optional(IDENTIFIER), "=",
                                innerTypeId)
                )
        );

        b.rule(typeParameterKey).is(
                b.firstOf(
                        RustKeyword.CLASS, // C++
                        RustKeyword.TYPENAME // C++
                )
        );

        b.rule(simpleTemplateId).is(
                templateName, "<",
                b.firstOf(
                        b.sequence(b.optional(templateArgumentList), ">"), // C++
                        b.sequence(innerTemplateId, ">>") // syntax sugar C++
                )
        );

        b.rule(innerTemplateId).is(
                b.zeroOrMore(b.nextNot(innerTypeId, ">>"), templateArgument, b.optional("..."), ","), innerTypeId
        );

        b.rule(innerTypeId).is(
                b.firstOf(
                        innerTrailingTypeSpecifier,
                        b.sequence(b.oneOrMore(typeSpecifier), innerTrailingTypeSpecifier),
                        b.sequence(typeSpecifierSeq, b.optional(noptrAbstractDeclarator), parametersAndQualifiers, "->",
                                innerTrailingTypeSpecifier)
                )
        );

        b.rule(innerTrailingTypeSpecifier).is(
                b.firstOf(
                        // simpleTypeSpecifier:
                        b.sequence(
                                b.optional("::"),
                                b.optional(nestedNameSpecifier),
                                b.optional(RustKeyword.TEMPLATE),
                                innerSimpleTemplateId
                        ),
                        // elaboratedTypeSpecifier:
                        b
                                .sequence(b.optional(cliAttributes), classKey, b.optional(nestedNameSpecifier), b
                                        .optional(RustKeyword.TEMPLATE), innerSimpleTemplateId),
                        // typenameSpecifier:
                        b.sequence(RustKeyword.TYPENAME, nestedNameSpecifier, RustKeyword.TEMPLATE, innerSimpleTemplateId)
                        // cvQualifier, cliDelegateSpecifier: never end with a template
                )
        );

        b.rule(innerSimpleTemplateId).is(
                templateName, "<", innerTemplateArgumentList
        );

        b.rule(templateId).is(
                b.firstOf(
                        simpleTemplateId, // C++
                        b.sequence(
                                b.firstOf(
                                        operatorFunctionId,
                                        literalOperatorId
                                ),
                                "<", b.optional(templateArgumentList), ">" // C++
                        ),
                        b.sequence(
                                b.firstOf(
                                        operatorFunctionId,
                                        literalOperatorId
                                ),
                                "<", innerTemplateId, ">>" // syntax sugar C++
                        )
                )
        );

        b.rule(templateName).is(
                IDENTIFIER // C++
        );

        b.rule(templateArgumentList).is(
                templateArgument, b.optional("..."), b.zeroOrMore(",", templateArgument, b.optional("...")) // C++
        );

        b.rule(innerTemplateArgumentList).is(
                innerTemplateArgument, b.optional("..."), b.zeroOrMore(",", innerTemplateArgument, b.optional("..."))
        );

        b.rule(templateArgument).is( //todo
                b.firstOf(
                        b.sequence(typeId, b.next(b.firstOf(">", ",", "..."))), // C++
                        b.sequence(typenameSpecifier, b.next(b.firstOf(">", ",", "..."))), // seen in gnu system headers
                        // FIXME: workaround to parse stuff like "carray<int, 10>"
                        // actually, it should be covered by the next rule (constantExpression)
                        // but it doesnt work because of ambiguity template syntax <--> relationalExpression
                        b.sequence(shiftExpression, b.next(b.firstOf(">", ",", "..."))),
                        b.sequence(constantExpression, b.next(b.firstOf(">", ",", "..."))) // C++
                        //        idExpression
                )
        );

        b.rule(innerTemplateArgument).is(
                b.firstOf(
                        b.sequence(typeId, b.next(b.firstOf(">>", ",", "..."))),
                        b.sequence(typenameSpecifier, b.next(b.firstOf(">>", ",", "..."))), // seen in gnu system headers
                        // FIXME: workaround to parse stuff like "carray<int, 10>", see above
                        b.sequence(additiveExpression, b.next(b.firstOf(">>", ","))),
                        b.sequence(constantExpression, b.next(b.firstOf(">>", ",")))
                        //        idExpression
                )
        );

        b.rule(typenameSpecifier).is( // todo
                b.firstOf(
                        b.sequence(
                                RustKeyword.TYPENAME, // C++
                                b.firstOf(
                                        b.sequence(nestedNameSpecifier, // C++
                                                b.firstOf(
                                                        b.sequence(b.optional(RustKeyword.TEMPLATE), simpleTemplateId), // C++
                                                        IDENTIFIER // C++
                                                )
                                        ),
                                        IDENTIFIER // todo
                                )
                        ),
                        IDENTIFIER // todo
                )
        );

        b.rule(explicitInstantiation).is(
                b.optional(RustKeyword.EXTERN), RustKeyword.TEMPLATE, declaration // C++
        );

        b.rule(explicitSpecialization).is(
                RustKeyword.TEMPLATE, "<", ">", declaration // C++
        );

        b.rule(deductionGuide).is(
                b.optional(RustKeyword.EXTERN), templateName, "(", parameterDeclarationClause, ")", "->", simpleTemplateId, ";" // C++ // todo EXTERN
        );
    }

    private static void generics(LexerfulGrammarBuilder b) {
        b.rule(cliGenericDeclaration).is(
                "generic", "<", cliGenericParameterList, ">", b.optional(cliConstraintClauseList), declaration
        );

        b.rule(cliGenericParameterList).is(
                cliGenericParameter, b.zeroOrMore(",", cliGenericParameter)
        );

        b.rule(cliGenericParameter).is(
                b.optional(attribute), b.firstOf(RustKeyword.CLASS, RustKeyword.TYPENAME), IDENTIFIER
        );

        b.rule(cliGenericId).is(
                cliGenericName, "<", cliGenericArgumentList, ">"
        );

        b.rule(cliGenericName).is(
                b.firstOf(IDENTIFIER, operatorFunctionId)
        );

        b.rule(cliGenericArgumentList).is(
                cliGenericArgument, b.zeroOrMore(",", cliGenericArgument)
        );

        b.rule(cliGenericArgument).is(
                typeId
        );

        b.rule(cliConstraintClauseList).is(
                cliConstraintClause, b.zeroOrMore(cliConstraintClause)
        );

        b.rule(cliConstraintClause).is(
                "where", IDENTIFIER, ":", cliConstraintItemList
        );

        b.rule(cliConstraintItemList).is(
                cliConstraintItem, b.zeroOrMore(",", cliConstraintItem)
        );

        b.rule(cliConstraintItem).is(
                b.firstOf(
                        typeId,
                        b.sequence(b.firstOf("ref", "value"), b.firstOf(RustKeyword.CLASS, RustKeyword.STRUCT)),
                        RustKeyword.GCNEW
                )
        );
    }

    // A.13 Exception handling
    //
    private static void exceptionHandling(LexerfulGrammarBuilder b) {

        b.rule(tryBlock).is(
                RustKeyword.TRY, compoundStatement, // C++
                b.firstOf(
                        b.sequence(handlerSeq, b.optional(cliFinallyClause)), // C++: handlerSeq; C++/CLI: cliFinallyClause
                        cliFinallyClause // C++/CLI
                )
        );

        b.rule(functionTryBlock).is(
                RustKeyword.TRY, b.optional(ctorInitializer), compoundStatement, // C++
                b.firstOf(
                        b.sequence(handlerSeq, b.optional(cliFinallyClause)), // C++: handlerSeq; C++/CLI: cliFinallyClause
                        cliFinallyClause // C++/CLI
                )
        );

        b.rule(handlerSeq).is(
                b.oneOrMore(handler) // C++
        ).skipIfOneChild();

        b.rule(cliFinallyClause).is(
                "finally", compoundStatement // C++/CLI
        );

        b.rule(handler).is(
                RustKeyword.CATCH, "(", exceptionDeclaration, ")", compoundStatement // C++
        );

        b.rule(exceptionDeclaration).is(
                b.firstOf(
                        b.sequence(
                                b.optional(attributeSpecifierSeq), typeSpecifierSeq,
                                b.firstOf(
                                        declarator, // C++
                                        b.optional(abstractDeclarator) // C++
                                )
                        ),
                        b.sequence(b.optional(attributeSpecifierSeq), typeSpecifierSeq, b.optional(abstractDeclarator)), // C++
                        "..."
                )
        );

        b.rule(noexceptSpecifier).is(
                b.firstOf(
                        b.sequence(RustKeyword.NOEXCEPT, "(", constantExpression, ")"), // C++
                        RustKeyword.NOEXCEPT, // C++
                        b.sequence(RustKeyword.THROW, "(", b.optional(typeIdList), ")") // C++ / Microsoft: typeIdList
                )
        );

        b.rule(typeIdList).is(
                b.firstOf(
                        b.sequence(typeId, b.optional("..."), b.zeroOrMore(",", typeId, b.optional("..."))), // C++
                        "..."// Microsoft extension
                )
        );

    }

    private static void cliAttributes(LexerfulGrammarBuilder b) {
        b.rule(cliAttributes).is(
                b.oneOrMore(cliAttributeSection)
        );

        b.rule(cliAttributeSection).is(
                "[", b.optional(cliAttributeTargetSpecifier), cliAttributeList, "]"
        );

        b.rule(cliAttributeTargetSpecifier).is(
                cliAttributeTarget, ":"
        );

        b.rule(cliAttributeTarget).is(
                b.firstOf(
                        "assembly", RustKeyword.CLASS, "constructor", "delegate", RustKeyword.ENUM, "event", "field",
                        "interface", "method", "parameter", "property", "returnvalue", RustKeyword.STRUCT
                ));

        b.rule(cliAttributeList).is(
                cliAttribute, b.zeroOrMore(",", cliAttribute)
        );

        b.rule(cliAttribute).is(
                b.optional(nestedNameSpecifier), typeName, b.optional(cliAttributeArguments)
        );

        b.rule(cliAttributeArguments).is(
                "(",
                b.firstOf(
                        b.optional(cliPositionArgumentList),
                        b.sequence(cliPositionArgumentList, ",", cliNamedArgumentList),
                        cliNamedArgumentList
                ),
                ")"
        );

        b.rule(cliPositionArgumentList).is(
                cliPositionArgument, b.zeroOrMore(",", cliPositionArgument)
        );

        b.rule(cliPositionArgument).is(
                cliAttributeArgumentExpression
        );

        b.rule(cliNamedArgumentList).is(
                cliNamedArgument, b.zeroOrMore(",", cliNamedArgument)
        );

        b.rule(cliNamedArgument).is(
                IDENTIFIER, "=", cliAttributeArgumentExpression
        );

        b.rule(cliAttributeArgumentExpression).is(
                assignmentExpression
        );

    }

}
