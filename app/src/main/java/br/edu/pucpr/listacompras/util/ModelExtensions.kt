package br.edu.pucpr.listacompras.util

import br.edu.pucpr.listacompras.model.ItemCompra

/**
 * Extension functions for domain models
 * Encapsulates business logic close to the data they operate on
 */

/** Capitalizes the first letter of each word (e.g., "arroz integral" → "Arroz Integral") */
fun String.toTitleCase(): String = split(" ").joinToString(" ") { word ->
    word.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
}

/** Checks if the string is a valid item name (at least 2 non-blank characters after trimming) */
fun String.isValidItemName(): Boolean = trim().length >= 2

/** Generates an accessibility-friendly description for the item (includes category) */
fun ItemCompra.toAccessibilityDescription(): String {
    val status = if (isComprado) "comprado" else "pendente"
    return "$nome, $quantidade, $categoria, $status"
}

/** Generates a share-friendly text representation of the item */
fun ItemCompra.toShareText(): String {
    val check = if (isComprado) "✅" else "⬜"
    return "$check $nome — $quantidade [$categoria]"
}

/** Generates a share-friendly text for a list of items */
fun List<ItemCompra>.toShareText(): String {
    if (isEmpty()) return "Lista vazia"
    return joinToString("\n") { it.toShareText() }
}
