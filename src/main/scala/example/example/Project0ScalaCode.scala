package example;

// import scala.collection.JavaConverters._
// import org.mongodb.scala.connection.ClusterSettings

import org.mongodb.scala._
import example.Helpers._
import scala.io.Source

// Global functions

class File() {
  def getFilepath() : String = {
    println("Provide the relative path of the input file:")
    val filepath = readLine()
    return filepath
  }
}

class JSONData extends File {
  var JSONFilePath = getFilepath()
  def CheckFileisJSON(): String = {
    val extension = JSONFilePath.toString.split("\\.").last 
    if(extension != "json"){
        val flag: String = "N/A"
        return flag
    }
    else{
        println("Converting JSON data into a dataset:")
        return JSONFilePath
    }
  }
}


class MongoDBInstance(JSONFileData: String) {
    val jsonString = Source.fromFile(JSONFileData).getLines.toList
    val bsonDocuments = jsonString.map(doc => Document(doc))
    println("Starting MongoDB - Scala Demo...")
    //
    val client: MongoClient = MongoClient()
    val database: MongoDatabase = client.getDatabase("Project_0")

    // Get a Collection.
    println("Provide the desired name of the MongoDB collection:")
    val CollectionName = readLine()
    val collection: MongoCollection[Document] = database.getCollection(CollectionName)
    collection.insertMany(bsonDocuments).printResults()

    // Perform some calculations

    import org.mongodb.scala.model.Aggregates._
    import org.mongodb.scala.model.Projections._
    import org.mongodb.scala.model.Sorts._
    import org.mongodb.scala.model.Filters._

    // val observable: Observable[Completed] = collection.insertOne(doc)

    // Only when an Observable is subscribed to and data requested will the operation happen.
    // Explictly subscribe:
    // observable.subscribe(new Observer[Completed] {
    //  override def onNext(result: Completed): Unit = println("Inserted")
    //  override def onError(e: Throwable): Unit = println("Failed")
    //  override def onComplete(): Unit = println("Completed")
    //})

    // collection.update.Seq( {}, { set: {"new_field": 1}}, {multi:true})

    // collection.find().first().printHeadResult()

    // collection.insertOne(Document).results()
    client.close() 
}

// Execute

object ReadDemo{
  var LogFileName = "LogFile.txt"
  def main(args: Array[String]) {
    val currentDirectory = new java.io.File(".").getCanonicalPath
    println("The current working directory is:  " + currentDirectory)
    val JSON_Instance = new JSONData()
    val filename = JSON_Instance.CheckFileisJSON()
    if(filename == "N/A"){
        println("File is not a JSON file. Terminating program.")
    }
    else{
        println("File is valid.")
        val full_path_string = (os.pwd/"src"/"test"/"resources"/ filename).toString()
        val MakeMongoCollection = new MongoDBInstance(full_path_string)
    }
  }
}