package dk.joga.jogago.api

data class Category(
        val id: String,
        val image: String,
        val name: String
) {
    override fun toString(): String {
        return name
    }
}