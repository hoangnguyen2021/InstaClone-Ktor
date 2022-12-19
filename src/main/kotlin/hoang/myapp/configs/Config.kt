package hoang.myapp.configs

object Config {
    val mongoPw = System.getenv("MONGO_PW")
    val dbName = "instaclone-mongodb"
    val connectionString = "mongodb+srv://instaclone-mongodb:$mongoPw@cluster0.ltpeq78.mongodb.net/$dbName?retryWrites=true&w=majority"
}