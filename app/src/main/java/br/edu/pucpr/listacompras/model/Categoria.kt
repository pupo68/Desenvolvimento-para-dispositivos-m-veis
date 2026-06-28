package br.edu.pucpr.listacompras.model

/**
 * Categories for shopping list items
 * Each category has a display label and an icon resource for visual identification
 */
enum class Categoria(val label: String) {
    ALIMENTOS("Alimentos"),
    BEBIDAS("Bebidas"),
    LIMPEZA("Limpeza"),
    HIGIENE("Higiene"),
    OUTROS("Outros");

    override fun toString(): String = label

    companion object {
        /** Returns all category labels for Spinner/AutoComplete */
        val labels: List<String> = entries.map { it.label }

        /** Parses a label back to the enum, defaults to OUTROS */
        fun fromLabel(label: String): Categoria =
            entries.find { it.label.equals(label.trim(), ignoreCase = true) } ?: OUTROS
    }
}
