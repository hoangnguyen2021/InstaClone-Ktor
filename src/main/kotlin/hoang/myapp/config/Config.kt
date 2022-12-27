package hoang.myapp.config

object Config {
    // MongoDB
    val mongoPw = System.getenv("MONGO_PW")
    val dbName = "instaclone-mongodb"
    val connectionString = "mongodb+srv://instaclone-mongodb:$mongoPw@cluster0.ltpeq78.mongodb.net/$dbName?retryWrites=true&w=majority"

    val jwtSecret = System.getenv("JWT_SECRET")

    // Twilio
    val twilioUsername = "ACbe4922767caf3adf305e7005e3d6a76f"
    val twilioPassword = System.getenv("TWILIO_PW")
    val twilioVerifyServiceSid = "VAa709af5dd3d50cef29b2400f934509bd"
}