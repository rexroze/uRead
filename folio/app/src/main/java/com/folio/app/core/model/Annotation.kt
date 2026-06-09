package com.folio.app.core.model

import java.util.UUID

enum class AnnotationType { HIGHLIGHT, UNDERLINE, NOTE }
enum class AnnotationColor { YELLOW, GREEN, BLUE, PINK }

data class Annotation(
    val id: String = UUID.randomUUID().toString(),
    val mediaId: String,
    val type: AnnotationType,
    val selectedText: String,
    val note: String? = null,
    val pageOrPosition: Int = 0,
    val color: AnnotationColor = AnnotationColor.YELLOW,
    val createdAt: Long = System.currentTimeMillis(),
)
