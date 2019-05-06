#README for analysis.sc 

The purpose of the analysis.sc file is to perform a statistical analysis of the New Testament book *The Gospel according to John* using a method called n-grams.*

The purpose of this analysis is to see whether or not a translation lines up with the original text in what it is saying. It is asking the question, do these two texts in different languages actually talk about the same thing? N-grams is a method that a machine can process even chunks of a passage, and display common phrases within the text. Thus looking at a far distance, determine what the story is about without having to read the entire text.

An interesting result that came from running this file is that in the Greek and the English the most common 3-gram has something to do with speaking. Usually Jesus speaking; however, in the Spanish the most common 3-gram is "de los Judios" (26 times) which is interesting because it does not line up with the English or the Greek. It seems like the Spanish is more concerned with where people are from/who they are rather than the actions being taken within the story.

However, n-grams do not replace the idea of reading a text closely. For example, one of the ways that this finding above can be taken with a grain of salt is that some languages are more compact than others. Ancient Greek can say a lot more with one word than English or Spanish.

I can have confidence in these results because the function ngramHisto in the Scala programing language was written for the [OCHO2 library](https://github.com/cite-architecture/ohco2), written by Dr. Christopher Blackwell and Dr. Neel Smith, that runs the [CITE app](http://folio2.furman.edu/belton/?urn=urn:cite2:fufolio:mauldin_dse.v1:104), a digital tool that allows a user to upload a text, read it, and process it using methods like n-gram analysis. I have confidence in this method because of the use within this software where I got the same results as I did with the Scala script in this file.


---
\* To see a definition of what n-grams are when analyzing language please see https://en.wikipedia.org/wiki/N-gram
