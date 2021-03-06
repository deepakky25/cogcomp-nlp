/**
 * This software is released under the University of Illinois/Research and Academic Use License. See
 * the LICENSE file in the root folder for details. Copyright (c) 2016
 *
 * Developed by: The Cognitive Computation Group University of Illinois at Urbana-Champaign
 * http://cogcomp.cs.illinois.edu/
 */
package edu.illinois.cs.cogcomp.bigdata.lucene;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.core.LowerCaseFilter;
import org.apache.lucene.analysis.core.StopFilter;
import org.apache.lucene.analysis.en.EnglishPossessiveFilter;
import org.apache.lucene.analysis.en.PorterStemFilter;
import org.apache.lucene.analysis.miscellaneous.ASCIIFoldingFilter;
import org.apache.lucene.analysis.miscellaneous.WordDelimiterFilter;
import org.apache.lucene.analysis.standard.StandardFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.analysis.util.CharArraySet;
import org.apache.lucene.analysis.util.StopwordAnalyzerBase;
import org.apache.lucene.util.Version;

import java.io.Reader;

/**
 * For general purpose text analysis
 * @author cheng88
 *
 */
public final class MinimalAnalyzer extends StopwordAnalyzerBase {

    /**
     * Uses current lucene version
     */
    public MinimalAnalyzer(){
        super(Lucene.version);
    }
    
    public MinimalAnalyzer(Version version) {
        super(version);
    }
    
    /**
     * Creates an analyzer using custom stop words
     */
    public MinimalAnalyzer(CharArraySet stopwords){
        super(Lucene.version,stopwords);
    }

    @Override
    protected TokenStreamComponents createComponents(String fieldName, Reader reader) {
        final Tokenizer source = new StandardTokenizer(matchVersion, reader);
        TokenStream result = new StandardFilter(matchVersion, source);
        result = new ASCIIFoldingFilter(result);
        result = new LowerCaseFilter(matchVersion, result);
        result = new EnglishPossessiveFilter(matchVersion, result);
        result = new StopFilter(matchVersion, result, stopwords);
        result = new WordDelimiterFilter(result,WordDelimiterFilter.ALPHA,null);
        result = new PorterStemFilter(result);
        return new TokenStreamComponents(source, result);
    }
}