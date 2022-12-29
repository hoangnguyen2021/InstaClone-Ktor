package hoang.myapp.config

object Config {
    // MongoDB
    val MONGO_PW: String = System.getenv("MONGO_PW")
    const val DB_NAME = "instaclone-mongodb"
    val connectionString = "mongodb+srv://instaclone-mongodb:$MONGO_PW@cluster0.ltpeq78.mongodb.net/$DB_NAME?retryWrites=true&w=majority"

    val jwtSecret: String = System.getenv("JWT_SECRET")

    // Twilio
    const val TWILIO_USERNAME = "ACbe4922767caf3adf305e7005e3d6a76f"
    val TWILIO_PASSWORD: String = System.getenv("TWILIO_PW")
    const val TWILIO_VERIFY_SERVICE_SID = "VAa709af5dd3d50cef29b2400f934509bd"

    // SendinBlue
    val SENDIN_BLUE_API_KEY: String = System.getenv("SENDIN_BLUE_API_KEY")
}