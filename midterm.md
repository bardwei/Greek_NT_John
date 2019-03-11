#N-grams
##Ian Bardwell

A n-gram is the process of breaking down a document into even sections, for example a 3-gram in a document is breaking down the document in three word pieces, and displaying them for analysis. Some of the most common uses for n-grams is breaking down texts in the
humanities. For example, the most common 3-gram in the Spanish translation of the Gospel of John (RVR 1858) is "de los Judios" or "of the Jews." What breaking down this text into grams does is it helps find common trends throughout a text. N-grams are not just useful in the humanities but also in the sciences n-grams can be used for DNA sequencing[^note2]. Another use in the military world is using n-grams for codebreaking[^note3].


The first major thing that needs to be done when finding an n-gram is to strip the text of punctuation which I did using a replaceAll function, and I then used a .sliding function to analyze each unit in the text (words) one by one and chunking them into three word segments. I then turned that into a vector. When editing my n-gram program I realized that verse numbers had not been stripped from my noPunc vector. However, my colleague James Bergman pointed out that if I used the regex [0-9] it would strip the verse numbers from the text which allowed my n-grams to be more accurate by sliding across verses.

One interesting thing that can be done when analyzing an n-gram file is to use a .filter function to pull any n-grams that contain a certain value, for example in the Gospel of John, Jesus. This is because if Jesus is an important character in the Gospels using a 3-gram can help the reader understand what Jesus is doing. For example, the most common thing (11 times) Jesus does in the Spanish translation of the NT is "Jesus responded and," so we can inference from the n-gram that Jesus responds to situations/people a lot in the Gospel of John.


[note2]:https://en.wikipedia.org/wiki/N-gram
[note3]:https://en.wikipedia.org/wiki/N-gram
