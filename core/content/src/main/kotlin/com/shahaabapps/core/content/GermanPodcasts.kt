package com.shahaabapps.core.content

/**
 * The German A1 episodes, one for each topic. Maya explains in plain English
 * and Leo asks what a beginner would ask, as in the AZ-204 episodes; Jonas,
 * a native speaker, says every German word and sentence, so nothing a
 * learner copies is said with an English accent. A line of his that ends in
 * [repeat] is a drill: he says it slowly, leaves time to say it out loud,
 * then says it at normal speed. The transcript shows his German with the
 * English under it.
 *
 * Each episode teaches what its topic teaches and nothing else, ends with
 * what it means for the Goethe-Zertifikat A1 and the topic's own thing to try
 * today. GermanPodcastTest holds the shape; GermanGrammarTest reads Jonas's
 * German with everything else the course prints as correct.
 */
internal object GermanPodcasts {
    private val de1Words = Podcast(
        "de1-words",
        listOf(
            maya("Hi, and welcome to the first German episode. Today is greetings: hello, goodbye, please and thank you. And we have a new voice. Jonas grew up speaking German, so every German word you hear comes from him. When he leaves a gap, say it out loud with him."),
            jonas("Hallo! Ich bin Jonas. Schön, dich kennenzulernen.", "Hello! I'm Jonas. Nice to meet you."),
            leo("So when Jonas leaves a gap, I really say it, out loud? Even on the bus?"),
            maya("[chuckle] Quietly on the bus is fine. Saying it is how it sticks. Let's start with the polite hello, the one for people you don't know."),
            jonas("Guten Tag. [repeat]", "Good day: the polite hello."),
            leo("When do I use «Guten Tag» instead of just «Hallo»?"),
            maya("German keeps polite and friendly further apart than English does. The polite way is called «formell», formal, and it goes with «Sie», the polite word for you. For example, you walk into a bank in Berlin, a shop, the immigration office, or you meet a new colleague. «Guten Tag», and «Sie»."),
            jonas("Hallo! [repeat]", "Hello."),
            leo("And «Hallo» is for friends?"),
            maya("Yes. The friendly way is «informell», informal, and it goes with «du», the friendly word for you. Friends, family, children, your partner. Say you're greeting Sanya at home. That's «Hallo», and a friendly how are you."),
            jonas("Hallo Sanya, wie geht's? [repeat]", "Hi Sanya, how are you?"),
            leo("And what does Sanya say back?"),
            maya("The easy answer is, I'm well, thank you. Literally, it goes well for me."),
            jonas("Mir geht es gut, danke. [repeat]", "I'm well, thank you."),
            leo("What about the time of day? English has good morning and good evening."),
            maya("So does German, and they line up nicely. Good morning, good evening, good night. Listen for the small change in the last one."),
            jonas("Guten Morgen. Guten Abend. Gute Nacht. [repeat]", "Good morning. Good evening. Good night."),
            leo("[laugh] So it's «Guten Abend» when I arrive for dinner, and «Gute Nacht» when I leave?"),
            maya("Exactly, and that's a mistake people make. «Guten Abend» greets someone. «Gute Nacht» is what you say on the way out at night. Now goodbyes. They follow the same split as hello."),
            jonas("Tschüss, bis morgen! [repeat]", "Bye, see you tomorrow!"),
            leo("«Tschüss» sounds friendly. What do I say to a stranger?"),
            maya("The formal goodbye. It goes with «Sie» and «Guten Tag». Picture a small shop where you've just paid. You'd say this to the shop owner."),
            jonas("Auf Wiedersehen, Herr Müller. [repeat]", "Goodbye, Mr Müller."),
            leo("Okay. And the little words? Please and thank you?"),
            maya("Thank you is «danke». Please is «bitte». And here's the surprise: «bitte» also means you're welcome. So someone holds a door, you say «danke», and they say «bitte»."),
            jonas("Danke. Bitte. [repeat]", "Thank you. You're welcome."),
            leo("And yes and no?"),
            maya("«Ja» and «nein». A polite no is no, thank you. And one long word you'll use every day means both excuse me and sorry. For example, asking where the toilet is."),
            jonas("Nein, danke. Entschuldigung, wo ist die Toilette? [repeat]", "No, thank you. Excuse me, where is the toilet?"),
            leo("[sigh] That word is so long. Can he say it in pieces?"),
            maya("He'll say it slowly. Think of it as four small beats, ent, shul, dee, goong, and then put them together."),
            jonas("Entschuldigung. [repeat]", "Excuse me; sorry."),
            leo("So what should I remember for the Goethe exam?"),
            maya("Three things. In the speaking part you start by introducing yourself to examiners you've never met, so use «Guten Tag» and «Sie». In the listening part, the greeting tells you if people are friends or strangers: «Hallo» and «Tschüss», or «Guten Tag» and «Auf Wiedersehen». And «bitte» is both please and you're welcome."),
            leo("And something to try today?"),
            maya("Greet four people in German, out loud, choosing «du» or «Sie» before you speak. «Guten Tag» to someone you don't know, and «Hallo» to someone you do. Say «bitte» and «danke» at least once, and end one of them with «Auf Wiedersehen». Every word from today is now in your «Wortschatz», your word deck."),
            jonas("Tschüss! Bis bald!", "Bye! See you soon!"),
            leo("See you soon, then. See you next time."),
        ),
        series = PodcastSeries.GERMAN_A1,
    )

    val all: List<Podcast> = listOf(de1Words)
}
