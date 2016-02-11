package edu.illinois.cs.cogcomp.annotation;

import edu.illinois.cs.cogcomp.core.datastructures.ViewNames;
import edu.illinois.cs.cogcomp.core.datastructures.textannotation.TextAnnotation;
import edu.illinois.cs.cogcomp.core.datastructures.textannotation.View;

import java.util.Set;

/**
 * Specifies the operations to be supported by an AnnotatorService, which creates a TextAnnotation
 * from raw text and populates it with views generated by NLP components. Created by mssammon on
 * 9/22/15.
 */
public interface AnnotatorService {
    /**
     * A convenience method for creating a
     * {@link edu.illinois.cs.cogcomp.core.datastructures.textannotation.TextAnnotation}. Typically,
     * this will be accomplished using a {@link TextAnnotationBuilder}.
     *
     * @param text The raw text used to build the
     *        {@link edu.illinois.cs.cogcomp.core.datastructures.textannotation.TextAnnotation}
     *        where all the {@link edu.illinois.cs.cogcomp.core.datastructures.textannotation.View}s
     *        should be added.
     * @throws AnnotatorException If this service cannot provide this {@code viewName}
     */
    TextAnnotation createBasicTextAnnotation(String corpusId, String docId, String text)
            throws AnnotatorException;


    /**
     * A convenience method for creating a
     * {@link edu.illinois.cs.cogcomp.core.datastructures.textannotation.TextAnnotation} and adding
     * all the {@link edu.illinois.cs.cogcomp.core.datastructures.textannotation.View}s supported by
     * this {@link edu.illinois.cs.cogcomp.annotation.AnnotatorService}. This amounts to calling
     * {@link #createBasicTextAnnotation(String, String, String)} and successive calls of
     * {@link #addView(edu.illinois.cs.cogcomp.core.datastructures.textannotation.TextAnnotation, String)}
     *
     * @param text The raw text used to build the
     *        {@link edu.illinois.cs.cogcomp.core.datastructures.textannotation.TextAnnotation}
     *        where all the {@link edu.illinois.cs.cogcomp.core.datastructures.textannotation.View}s
     *        should be added.
     * @throws AnnotatorException If none of the {@code viewProviders} supports this
     *         {@code viewName}
     */
    TextAnnotation createAnnotatedTextAnnotation(String corpusId, String textId, String text)
            throws AnnotatorException;

    /**
     * An overloaded version of {@link #createAnnotatedTextAnnotation(String, String, String)} that
     * adds only the {@link edu.illinois.cs.cogcomp.core.datastructures.textannotation.View}s
     * requested.
     *
     * @param text The raw text used to build the
     *        {@link edu.illinois.cs.cogcomp.core.datastructures.textannotation.TextAnnotation}
     *        where all the {@link edu.illinois.cs.cogcomp.core.datastructures.textannotation.View}s
     *        should be added.
     * @param viewNames Views to add
     * @throws AnnotatorException If none of the {@code viewProviders} supports this
     *         {@code viewName}
     */
    TextAnnotation createAnnotatedTextAnnotation(String corpusId, String textId, String text,
            Set<String> viewNames) throws AnnotatorException;


    /**
     * The standard way of adding a specific
     * {@link edu.illinois.cs.cogcomp.core.datastructures.textannotation.View} to a
     * {@link edu.illinois.cs.cogcomp.core.datastructures.textannotation.TextAnnotation}.
     *
     * @param ta The
     *        {@link edu.illinois.cs.cogcomp.core.datastructures.textannotation.TextAnnotation}
     *        where the {@link edu.illinois.cs.cogcomp.core.datastructures.textannotation.View}
     *        should be added.
     * @param viewName The name of the
     *        {@link edu.illinois.cs.cogcomp.core.datastructures.textannotation.View} to be added.
     *        By convention this has to be a constant in
     *        {@link edu.illinois.cs.cogcomp.core.datastructures.ViewNames}.
     * @return 'true' if the TextAnnotation was modified by this call.
     * @throws AnnotatorException If this AnnotatorService cannot provide this {@code viewName},
     */
    boolean addView(TextAnnotation ta, String viewName) throws AnnotatorException;
}
