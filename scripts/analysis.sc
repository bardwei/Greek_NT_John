
import scala.io.Source
import java.io._
import scala.collection.mutable.LinkedHashMap
import edu.holycross.shot.scm._
import edu.holycross.shot.cite._
import edu.holycross.shot.citeobj._
import edu.holycross.shot.ohco2._
import edu.holycross.shot.seqcomp._
import edu.furman.classics.citealign._

val demoLib:String = "john.cex"

def loadLibrary(fp:String = demoLib):CiteLibrary = {
	val library = CiteLibrary(Source.fromFile(fp).getLines.mkString("\n"),"#",",")
	library
}


def saveString(s:String, filePath:String = "html/", fileName:String = "john.cex"):Unit = {
	val pw = new PrintWriter(new File(filePath + fileName))
	for (line <- s.lines){
		pw.append(line)
		pw.append("\n")
	}
	pw.close
}

/* Project-specific CEX Stuff */

val myCexFile:String = "johnAll.cex"

lazy val lib = loadLibrary(myCexFile)
lazy val tr = lib.textRepository.get
lazy val johnCorpus = tr.corpus


val johnStr:String = "urn:cts:greekLit:tlg0031.tlg004:"
val englishStr:String = "urn:cts:greekLit:tlg0031.tlg004.kjv_fu:"
val spanishStr:String = "urn:cts:greekLit:tlg0031.tlg004.reina:"
val greekStr:String = "urn:cts:greekLit:tlg0031.tlg004.wh_fu:"
val lemTokStr:String = "urn:cts:greekLit:tlg0031.tlg004.wh_fu.lemmatizedToken:"
val lemStr:String = "urn:cts:greekLit:tlg0031.tlg004.wh_fu.lemmatizedToken_merged:"

val c:Corpus = johnCorpus

//Analyzing entire corpus

val sh:StringHistogram = c.ngramHisto(3,10)

println(sh)

//creating corpora for the Greek histograms

val lemCorp:Corpus = c >= CtsUrn(lemStr)
val lemTokCorp:Corpus = c >= CtsUrn(lemTokStr)
val greekCorp:Corpus = ((c ~~  CtsUrn(greekStr)) -- lemCorp) -- lemTokCorp

//Creating English and Spanish corpora to break down individual language histograms

val spanishCorp:Corpus = c ~~ CtsUrn(spanishStr)
val englishCorp:Corpus = c ~~ CtsUrn(englishStr)

//Creating histograms for each individual corpus

val lemHisto:StringHistogram = lemCorp.ngramHisto(3,10)

println(lemHisto)

val lemTokHisto:StringHistogram = lemTokCorp.ngramHisto(3,10)

println(lemTokHisto)

val greekHisto:StringHistogram = greekCorp.ngramHisto(3,10)

println(greekHisto)

val spanishHisto:StringHistogram = spanishCorp.ngramHisto(3,10)

println(spanishHisto)

val englishHisto:StringHistogram = englishCorp.ngramHisto(3,10)

println(englishHisto)
