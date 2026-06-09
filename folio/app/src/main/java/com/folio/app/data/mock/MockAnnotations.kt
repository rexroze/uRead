package com.folio.app.data.mock

import com.folio.app.core.model.Annotation
import com.folio.app.core.model.AnnotationColor
import com.folio.app.core.model.AnnotationType

object MockAnnotations {
    val annotations: MutableList<Annotation> = mutableListOf(
        Annotation(
            id = "ann1",
            mediaId = "1",
            type = AnnotationType.HIGHLIGHT,
            selectedText = "The light at the end of the day fell in long bars across the floor",
            note = null,
            pageOrPosition = 45,
            color = AnnotationColor.YELLOW,
        ),
        Annotation(
            id = "ann2",
            mediaId = "1",
            type = AnnotationType.NOTE,
            selectedText = "She turned the page without thinking",
            note = "Beautiful imagery — reminds me of slow Sunday afternoons",
            pageOrPosition = 45,
            color = AnnotationColor.BLUE,
        ),
        Annotation(
            id = "ann3",
            mediaId = "1",
            type = AnnotationType.UNDERLINE,
            selectedText = "The paper soft from a hundred readings before hers",
            note = null,
            pageOrPosition = 46,
            color = AnnotationColor.YELLOW,
        ),
    )

    fun forMedia(mediaId: String): List<Annotation> = annotations.filter { it.mediaId == mediaId }

    fun add(annotation: Annotation) = annotations.add(annotation)

    fun remove(id: String) = annotations.removeAll { it.id == id }
}
