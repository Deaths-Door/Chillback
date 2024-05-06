@Deprecated("Remove this bullshit")
interface Platform {
    val name: String
}

expect fun getPlatform(): Platform