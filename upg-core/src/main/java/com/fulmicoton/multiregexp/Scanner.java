package com.fulmicoton.multiregexp;

import java.io.CharArrayReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;

import org.apache.commons.lang3.StringUtils;

/* https://github.com/fulmicoton/multiregexp */
public class Scanner<T> {


    private static final int BUFFER_NUM_BITS = 8;
    private static final int MASK = (1 << BUFFER_NUM_BITS) - 1;

    private final MultiPatternAutomaton automaton;

    private final char[] circularBuffer = new char[1 << BUFFER_NUM_BITS];
    public Reader reader;

    private boolean endOfReader = false;
    private final ArrayList<T> tokenTypes;
    // private int cursor;
    private int readUntil;

    public T type;
    public int start = 0;
    public int end = 0;
    public int readerLength = Integer.MAX_VALUE;

    public int line = 1;
    public int startColumnToken = 1;
    public int endColumnToken = 1;

    public void reset(final Reader reader) {
        this.reader = reader;
        this.start = 0;
        this.end = 0;
        this.endOfReader = false;
        this.type = null;
        this.readerLength = Integer.MAX_VALUE;
        this.readUntil = 0;
    }

    private static Reader readerFromCharSequence(final CharSequence charSeq) {
        final int numChars = charSeq.length();
        final char[] chars = new char[numChars];
        for (int i=0; i<numChars; i++) {
            chars[i] = charSeq.charAt(i);
        }
        return new CharArrayReader(chars);
    }

    public Scanner(final MultiPatternAutomaton automaton,
                   final CharSequence charSequence,
                   final ArrayList<T> tokenTypes) {
        this(automaton, readerFromCharSequence(charSequence), tokenTypes);
    }

    public Scanner(final MultiPatternAutomaton automaton,
                   final Reader reader,
                   final ArrayList<T> tokenTypes) {
        this.automaton = automaton;
        this.reader = reader;
        this.tokenTypes = tokenTypes;
    }


    /**
     * Same as next(), but throws unchecked Exception.
     */
    boolean nextUnchecked() {
        try {
            return this.next();
        } catch (final ScanException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void put(final int i, final char c) {
        this.circularBuffer[i & MASK] = c;
    }

    private char get(final int i) {
        return this.circularBuffer[i & MASK];
    }

    private char readOne(final int i) throws IOException {
        if (i < this.readUntil) {
            return this.circularBuffer[i & MASK];
        }
        if (i == this.readUntil) {
            if (this.endOfReader) {
                return 0;
            }
            final int cInt = this.reader.read();
            if (cInt < 0) {
                this.endOfReader = true;
                this.readerLength = i;
                return 0;
            }
            else {
                this.readUntil += 1;
                final char chr = (char)cInt;
                this.put(i, chr);
                return chr;
            }
        }
        throw new IOException("");

    }

    public boolean next() throws ScanException, IOException {
        // we start at the end of the last emitted token
        if (this.end == this.readerLength) {
            return false;
        }
        
        this.start = this.end;
        this.startColumnToken = this.endColumnToken;
        
        int p = 0;
        int highestPriorityMatch = Integer.MAX_VALUE;
        int lastLetter = start;

        for (int cursor = start; cursor < this.readerLength; cursor++) {
            final char chr = this.readOne(cursor);
            if (chr == 0) {
                break;
            }
            p = this.automaton.step(p, chr);
            if (p == -1) {
                break;
            }
            else {
                final int[] accept = this.automaton.accept[p];
                if (accept.length > 0) {
                    final int minAccept = accept[0];
                    if (minAccept <= highestPriorityMatch) {
                        // HighPriority = low value.
                        // If we find a match with a higher priority
                        // we prefer than one,
                        // If it is the same pattern which is
                        // match we take that too for the sake of greediness.
                        highestPriorityMatch = minAccept;
                        lastLetter = cursor;
                    }
                }
                // when a match is found, we keep matching
                // as a longer prefix might match a pattern
                // with a higher priority.
            }

        }


        // No tokens have been found. Raised an expression
        // with a bit of context, and the offset in the string.
        if (highestPriorityMatch == Integer.MAX_VALUE) {
            /*if (this.start == 0) {
                return false;
            }*/
            final int contextStart = Math.max(0, this.start - 10);
            final int contextEnd = Math.min(this.start + 10, this.readUntil);
            final String context = this.subSequence(contextStart, this.start) + "|" +  this.subSequence(this.start, contextEnd);
            throw new ScanException(context, this.startColumnToken, this.endColumnToken, this.line, this.subSequence(this.start, contextEnd).toString());
        }
        
        this.end = lastLetter + 1;
        this.type = this.tokenTypes.get(highestPriorityMatch);
        
        this.endColumnToken += tokenString().length();
        
        if (StringUtils.countMatches(tokenString(), '\n') > 0) {
        	line += StringUtils.countMatches(tokenString(), '\n');
        	this.startColumnToken = 1;
        	this.endColumnToken = 1;
        }
        
        if (StringUtils.countMatches(tokenString(), '\t') > 0) {       
        	this.endColumnToken += (this.end - this.start);
        }
        
        return true;
    }

    private CharSequence subSequence(final int start, final int end) {
        return new CharSeq(this.circularBuffer, start, end-start);
    }


    public static class CharSeq implements CharSequence {
        private final char[] buffer;
        private final int start;
        private final int length;

        public CharSeq(final char[] buffer, final int start, final int length) {
            this.buffer = buffer;
            this.start = start;
            this.length = length;
        }

        public String toString() {
            return new StringBuilder(this).toString();
        }

        @Override
        public int length() {
            return this.length;
        }

        @Override
        public char charAt(final int index) {
            return this.buffer[(this.start + index) & MASK];
        }

        @Override
        public CharSequence subSequence(final int newStart, final int newEnd) {
            return new CharSeq(this.buffer, (this.start + newStart) & MASK, newEnd - newStart);
        }
    }

    public CharSequence tokenString() {
        return new CharSeq(this.circularBuffer, this.start, this.end - this.start);
    }
}
