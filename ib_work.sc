import scala.io.Source
import edu.holycross.shot.cite

// Fun with Collections

/*
val myList = List("a","b","c")
val myArray = Array("a","b","c")
val myVector = Vector("a","b","c")*/




val filepath:String = "/vagrant/NT_John/NT_RVR_1858.txt"
val myBook:Vector[String] = Source.fromFile(filepath).getLines.toVector.filter(_.size > 0)

val wordVec:Vector[String] = {
  val bigString:String = myBook.mkString(" ")
  val noPunc:String = bigString.replaceAll("""[\[\]¿¡,.?;:!"]""","").replaceAll(" +", " ")
  val tokenized:Vector[String] = noPunc.split(" ").toVector.filter( _.size > 0)
  tokenized
}

/*word histogram */
val wordMap:Map[String,Vector[String]] = wordVec.groupBy(w => w)
val quantMap:Map[String,Int] = wordMap.map(m => (m._1, m._2.size))
val mapVec:Vector[(String,Int)] = quantMap.toVector
val wordHisto = mapVec.sortBy(_._2).reverse
val uniqueWords:Vector[String] = wordHisto.map(_._1)

println(s"\n\n-------\nThere are ${wordHisto.size} unique words.\n------\n")

/*for( wh <- wordHisto) println(s"${wh._1} = ${wh._2}")

val maxNum:Int = wordHisto.head._2
val divisor:Int = maxNum / 75
for ( wh <- wordHisto) {
  val stars:Int = wh._2 / divisor
  if (stars > 0 ) {
  println()
  print(wh._1.padTo(20, " ").mkString(""))
  for (x <- 1 to stars) print("*")
}
}*/

/*how to compare percentage*/
println()

val uniquePrecent = wordHisto.filter(_._2 == 1).size.toDouble / wordVec.size.toDouble * 100


println(s"\n\n-------\nThere are ${uniquePrecent} percentage of unique words in your document.\n-------\n")

/*Vector: [String, Int] -> Vector:String
  wordHisto.map(_._1)*/

/*val romanNum:Map[Int, String] = Map( 1 -> "I", 2 -> "II", 3 -> "III", 4 -> "IV", 5 -> "V", 6 -> "VI", 7 -> "VII", 8 -> "VIII", 9 -> "IX", 10 -> "X", 11 -> "XI", 12 -> "XII", 13 -> "XIII", 14 -> "XIV", 15 -> "XV", 16 -> "XVI", 17 -> "XVII", 18 -> "XVIII", 19 -> "XIX", 20 -> "XX")


val testInt1 = 13
println(s"""${testInt1} = XIII: ${romanNum(13) == "XIII"}""")*/

/* Spell Check! */
val dictpath:String = "/vagrant/NT_John/words.txt"
val lexEntries:Vector[String] = Source.fromFile(dictpath).getLines.toVector.filter(_.size > 0)

val badWords:Vector[String] = uniqueWords.filter( w => {
  lexEntries.contains(w.toLowerCase) == false &
  lexEntries.contains(w)
})

for (w <- badWords) {
  println(w)
}