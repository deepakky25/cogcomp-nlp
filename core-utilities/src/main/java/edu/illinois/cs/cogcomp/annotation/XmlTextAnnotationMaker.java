package edu.illinois.cs.cogcomp.annotation;

import edu.illinois.cs.cogcomp.core.datastructures.IntPair;
import edu.illinois.cs.cogcomp.core.datastructures.Pair;
import edu.illinois.cs.cogcomp.core.datastructures.textannotation.TextAnnotation;
import edu.illinois.cs.cogcomp.core.datastructures.textannotation.XmlTextAnnotation;
import edu.illinois.cs.cogcomp.core.utilities.StringTransformation;
import edu.illinois.cs.cogcomp.core.utilities.TextCleanerStringTransformation;
import edu.illinois.cs.cogcomp.nlp.tokenizer.Tokenizer;

import java.util.Map;
import java.util.Set;

/**
 * Instantiates a XmlTextAnnotation object from xml text. The xml is parsed into body text (which is further cleaned
 *    up as needed), and this cleaned text is used to create a TextAnnotation. Additional information is extracted
 *    from the xml source. The mapping between the xml source and the cleaned text (i.e. mapping between character
 *    offsets) is also derived.
 * The goal is to provide text that can be processed easily with an NLP pipeline without a lot of hacks to work around
 *    ill-formatted text. The annotations so produced can then be mapped to the offsets in the original xml text,
 *    and combined with supplementary information extracted from the xml markup.
 *
 * @author mssammon
 */

public class XmlTextAnnotationMaker {

    private final Set<String> tagsWithText;
    private final Map<String, Set<String>> tagsWithAttributes;
    private final TextAnnotationBuilder taBuilder;


    /**
     * Specifies the behavior of the XmlTextAnnotationMaker: tokenization (via the TextAnnotationBuilder),
     *    which xml tags to use for body text and for retained attributes
     * @param taBuilder generates the sentence split and tokenized text for further processing
     * @param tagsWithText xml tags that delimit text to be processed
     * @param tagsWithAttributes xml tags with attributes that must be preserved
     */
    public XmlTextAnnotationMaker(TextAnnotationBuilder taBuilder, Set<String> tagsWithText,
                                  Map<String, Set<String>> tagsWithAttributes) {
        this.taBuilder = taBuilder;
        this.tagsWithText = tagsWithText;
        this.tagsWithAttributes = tagsWithAttributes;
    }



    /**
     * A method for creating
     * {@link TextAnnotation} by
     * tokenizing the given text string.
     *
     * @param xmlText Raw xml text from corpus docuemnt
     * @param corpusId corpus identifier
     * @param docId text identifier
     * @return an XmlTextAnnotation with the cleaned text (StringTransformation), TextAnnotation for
     *          the cleaned text, and xml markup extracted from source
     */
    public XmlTextAnnotation createTextAnnotation(String xmlText, String corpusId, String docId)  {

        StringTransformation xmlSt = new StringTransformation(xmlText);
        Pair<StringTransformation, Map<IntPair, Map<String, String>>> cleanResults =
                TextCleanerStringTransformation.cleanDiscussionForumXml(xmlSt, tagsWithText, tagsWithAttributes);

        TextAnnotation ta = taBuilder.createTextAnnotation(corpusId, docId, xmlSt.getTransformedText());

        return new XmlTextAnnotation(cleanResults.getFirst(), ta, cleanResults.getSecond());
    }

}
