/**
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.elegoff.plugins.rust.linecounter;

import java.util.ArrayList;
import java.util.List;

/**
 * Main RUST parser utility class. Use the methods of this class to tokenize a RUST string.
 */
public class Parser {
    /**
     * All classes that implement this interface can expose a line number
     */
    public interface Lined {
        int getLineNo();
    }

    /**
     * Simple wrapper class for lines: a line is contained within a given string (the boundaries are given
     * by <var>start</var> and <var>end</var>) and has a line number
     */
    public static class Line implements Lined {
        private int lineNo;
        private int start;
        private int end;
        private String buffer;


        /**
         * Constructor
         *
         * @param lineNo the line number
         * @param buffer the string in which the line can be found
         * @param start  the start index of the line in <var>buffer</var>
         * @param end    the end index of the line in <var>buffer</var>
         */
        public Line(int lineNo, String buffer, int start, int end) {
            this.lineNo = lineNo;
            this.start = start;
            this.end = end;
            this.buffer = buffer;
        }

        /**
         * Returns the actual line content
         *
         * @return the line content taken from the buffer between <var>start</var> and <var>end</var>
         */
        public String getContent() {
            return buffer.substring(start, end);
        }

        @Override
        public int getLineNo() {
            return lineNo;
        }

        /**
         * Returns the end index
         *
         * @return the end index
         */
        public int getEnd() {
            return end;
        }

        /**
         * Returns the start index
         *
         * @return the start index
         */
        public int getStart() {
            return start;
        }

        /**
         * Returns the buffer in which we can find the line
         *
         * @return the buffer in which we can find the line
         */
        public String getBuffer() {
            return buffer;
        }
    }

    /**
     * Simple wrapper class for token: a token is found on a line and has neighbour tokens
     */
    public static class Token implements Lined {
        private int lineNo;

        @Override
        public int getLineNo() {
            return lineNo;
        }
    }

    /**
     * Simple wrapper class for comments: a RUST comment start with a {@code #} character, is located on a line
     * and starts at a given column number on this line. It also has tokens located before and after it.
     */
    public static class Comment implements Lined {
        private int lineNo;
        private int columnNo;
        private int pointer;
        private String buffer;
        private Comment commentBefore;

        @Override
        public String toString() {
            int end = buffer.indexOf('\n', pointer);
            if (end == -1) {
                end = buffer.indexOf('\0', pointer);
            }
            if (end != -1) {
                return buffer.substring(pointer, end);
            }
            return buffer.substring(pointer);
        }

        @Override
        public boolean equals(Object other) {
            return other instanceof Comment &&
                    lineNo == ((Comment) other).lineNo &&
                    columnNo == ((Comment) other).columnNo &&
                    toString().equals(other.toString());
        }

        @Override
        public int hashCode() {
            return (lineNo + ":" + columnNo + ":" + toString()).hashCode();
        }

        @Override
        public int getLineNo() {
            return lineNo;
        }

        /**
         * Returns the column number at which the comment starts
         *
         * @return the column number at which the comment starts
         */
        public int getColumnNo() {
            return columnNo;
        }

        /**
         * Returns the end index of the comment in <var>buffer</var>
         *
         * @return the end index of the comment in <var>buffer</var>
         */
        public int getPointer() {
            return pointer;
        }


        /**
         * Returns the possible comment located before this comment
         *
         * @return the possible comment located before this comment
         */
        public Comment getCommentBefore() {
            return commentBefore;
        }

        public String getBuffer() {
            return buffer;
        }
    }


    /**
     * Hide constructor, only static methods
     */
    private Parser() {
    }


    /**
     * Parses the passed string and returns the lines found in this text
     *
     * @param buffer a string to be parsed
     * @return the list of lines found in the string
     */
    public static List<Line> getLines(String buffer) {
        List<Line> lines = new ArrayList<>();
        int lineNo = 1;
        int cur = 0;
        int next = buffer.indexOf('\n');
        while (next != -1) {
            lines.add(new Line(lineNo, buffer, cur, next));
            cur = next + 1;
            next = buffer.indexOf('\n', cur);
            lineNo += 1;
        }

        lines.add(new Line(lineNo, buffer, cur, buffer.length()));

        return lines;
    }
}