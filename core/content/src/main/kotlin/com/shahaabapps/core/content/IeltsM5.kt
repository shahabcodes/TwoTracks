package com.shahaabapps.core.content

/** IELTS module 5: General Training Writing Task 1, the letter. */
internal object IeltsM5 {
    private val refs = Ielts.commonRefs

    private val tone = Topic(
        id = "ie5-tone", title = "Three tones, one structure: choosing formal, semi-formal or informal", minutes = 24, references = refs + B.ref("General Training Writing: the format in detail", Ielts.ORG + "take-a-test/test-types/ielts-general-training-test/ielts-general-training-format-writing") + B.ref("General Training sample questions, paper by paper", Ielts.ORG + "take-a-test/preparation-resources/sample-test-questions/general-training-test"),
        blocks = listOf(
            B.warmup(
                "ie5-tone-w1", "ie5-tone",
                "A letter opens 'Dear Sir or Madam'. Which sign-off do you think goes with it?",
                listOf("Yours sincerely", "Yours faithfully", "Either is fine"), 1,
                "The pairing turns on one thing: whether you used a name.",
            ),
            B.plain("General Training Task 1 is a letter: 150 words in 20 minutes, with three bullet points you must cover. The first decision is tone, because it changes the greeting, the sign-off and nearly every sentence after that. Get it wrong and Task Achievement drops however good the English."),
            B.diagram(
                caption = "Who is the letter to? That single question sets the tone, and the tone sets the opening and closing.",
                nodes = listOf(
                    DNode("q", "Who receives it?", 0.5f, 0.12f, w = 0.4f, h = 0.16f, tone = 1),
                    DNode("f", "Formal: a stranger in an organisation", 0.17f, 0.6f, w = 0.3f, h = 0.44f, sub = "Dear Sir or Madam → Yours faithfully"),
                    DNode("s", "Semi-formal: a named person you know slightly", 0.5f, 0.6f, w = 0.3f, h = 0.44f, tone = 2, sub = "Dear Mr Brown → Yours sincerely"),
                    DNode("i", "Informal: a friend or family", 0.83f, 0.6f, w = 0.3f, h = 0.44f, sub = "Dear Anna / Hi Anna → Best wishes"),
                ),
                edges = listOf(DEdge("q", "f"), DEdge("q", "s"), DEdge("q", "i")),
                aspect = 0.52f,
            ),
            B.table(
                listOf("", "Formal", "Semi-formal", "Informal"),
                listOf("Typical task", "Complaint to a company, application, request to a council", "Letter to a manager, landlord or tutor you have met", "Letter to a friend: invitation, news, apology, advice"),
                listOf("Greeting", "Dear Sir or Madam,", "Dear Mr Brown, / Dear Dr Khan,", "Dear Anna, / Hi Anna,"),
                listOf("Sign-off", "Yours faithfully,", "Yours sincerely,", "Best wishes, / All the best, / Love,"),
                listOf("Contractions", "Never: I am writing", "Avoid", "Yes: I'm writing"),
                listOf("Phrasal verbs", "Avoid: tolerate, postpone", "Some", "Yes: put up with, put off"),
                listOf("Opening line", "I am writing to complain about…", "I am writing to ask whether…", "How are you? I thought I'd write because…"),
            ),
            B.remember("Dear Sir or Madam pairs with Yours faithfully. A name pairs with Yours sincerely. Mixing them is the most common avoidable error in this task."),
            B.h("The four paragraphs"),
            B.table(
                listOf("Paragraph", "Content"),
                listOf("1 Opening", "Why you are writing, in one sentence, plus a greeting line if informal"),
                listOf("2 Bullet one", "Covered fully, with a detail you invent"),
                listOf("3 Bullets two and three", "Or one paragraph each if you have room"),
                listOf("4 Closing", "A forward-looking line: I look forward to hearing from you / Write back soon"),
            ),
            B.p("You may invent any detail you need: names, dates, prices, flat numbers. Examiners do not check facts; they check that each bullet is covered fully and convincingly. Vague answers lose marks, invented specifics do not."),
            B.check("ie5-tone-k1", "ie5-tone", "A letter to the manager of a shop whose name you do not know should end with", listOf("Yours sincerely", "Yours faithfully", "Best wishes"), 1, "No name means Dear Sir or Madam and Yours faithfully."),
            B.check("ie5-tone-k2", "ie5-tone", "Which opening suits an informal letter?", listOf("I am writing to express my dissatisfaction.", "Hi Anna, how are things? I thought I'd write because I've got some news.", "I wish to draw your attention to a matter."), 1, "Contractions, a question to the reader and everyday vocabulary mark the informal register."),
            B.check(
                "ie5-tone-k3", "ie5-tone",
                "Your formal letter needs a date and an order number that the task did not give you. You should",
                listOf("invent them", "write 'date unknown'", "leave the bullet uncovered"), 0,
                "Invented detail is expected and is never checked. What is marked is whether the bullet was covered, and a specific detail covers it better than a vague one.",
                whyWrong = mapOf(
                    1 to "That reads as an unfinished letter and covers nothing.",
                    2 to "A missing bullet is the heaviest penalty in this task.",
                ),
            ),
            B.worked("""
                Worked example. The tone decision, made once, settles everything after it. The task says: write to the
                manager of a company about a faulty laptop. You do not have a name, so the greeting is Dear Sir or
                Madam and the sign-off is Yours faithfully. Formal means no contractions, so I am writing rather than
                I'm writing, and no phrasal verbs, so tolerate rather than put up with. The opening line states the
                purpose in one sentence: I am writing to complain about a laptop purchased from your Leeds branch.
            """),
            B.faded(
                "ie5-tone-f1",
                "A different task, the same decision. Write to Dr Khan, your course tutor, whom you have met several times, to ask for an extension on an assignment.",
                listOf(
                    "You have a name, so the greeting takes it: Dear Dr Khan,",
                    "A name pairs with one closing and only one: Yours ___,",
                    "Semi-formal means no contractions and few phrasal verbs: I am writing to ask whether the deadline could be extended.",
                    "The closing line looks forward: I would be grateful for any advice you are able to offer.",
                ),
                1, listOf("sincerely"),
                "Dear Sir or Madam with Yours faithfully; a name with Yours sincerely. Mixing the halves is the most common avoidable error in the whole task.",
            ),
            B.vocab(
                B.w("ie5-faithfully", "Yours faithfully", "formal close after Dear Sir or Madam", "Yours faithfully, U. Shahab", "Used when the name is unknown."),
                B.w("ie5-sincerely", "Yours sincerely", "formal close after a name", "Yours sincerely, Sanya Khan", "Used when you address a person by name."),
                B.w("ie5-register", "register", "how formal the language is", "Keep the register consistent.", "Mixing registers costs marks."),
                B.w("ie5-contraction", "contraction", "I'm, don't, it's", "Contractions suit informal letters only.", "Write them out in formal ones."),
                B.w("ie5-phrasal", "phrasal verb", "a verb plus a particle", "Put up with is informal; tolerate is formal.", "Choose by register."),
                B.w("ie5-forward-looking", "forward-looking close", "a final line about what happens next", "I look forward to your reply.", "Expected in formal letters."),
            ),
            B.tryIt("Try it in ten minutes", "Three openings and three closings, written out from memory.",
                "Formal pair, with no name", "Semi-formal pair, with a name", "Informal pair", "Rewrote one formal opening as informal, changing every contraction",
            ),
            B.mistakes(
                "Dear Sir or Madam with Yours sincerely. No name means Yours faithfully.",
                "Contractions in a formal letter: I'm writing rather than I am writing.",
                "Hedging about a detail the task did not give you. Invent it; nothing is checked.",
                "Choosing the tone from the topic rather than from who the letter is to.",
            ),
            B.explain(
                "ie5-tone-x1",
                "Explain how the tone of a General Training letter is decided, and what follows from that decision.",
                "The tone is decided by who the letter is to, not by what it is about. An unnamed company takes Dear Sir or Madam and Yours faithfully; somebody you have met and can name takes Dear Mr Brown and Yours sincerely; a friend takes a first name and Best wishes. Everything after follows: no contractions and no phrasal verbs in a formal letter, both in an informal one. Any detail the task did not give you may simply be invented.",
                "Who it is to decides the tone" to listOf("who the letter is to", "who it is to", "not by what"),
                "The greeting and the sign-off have to match" to listOf("yours faithfully", "yours sincerely", "sign-off"),
                "Missing detail is invented, not avoided" to listOf("invented", "invent"),
            ),
            B.glossary("Bullet point" to "One of the three things the task tells you to include.", "Semi-formal" to "Polite but personal: a named person you know slightly.", "Invented detail" to "A specific you make up to cover a bullet fully."),
            B.recap("Decide the tone from the reader, then the greeting and sign-off follow.", "Dear Sir or Madam with Yours faithfully; a name with Yours sincerely.", "Four paragraphs, all three bullets covered, invented details welcome."),
        ),
    )

    private val types = Topic(
        id = "ie5-types", title = "The five letters that keep appearing, with the sentences to reuse", minutes = 26, references = refs + B.ref("General Training Writing: the format in detail", Ielts.ORG + "take-a-test/test-types/ielts-general-training-test/ielts-general-training-format-writing") + B.ref("General Training sample questions, paper by paper", Ielts.ORG + "take-a-test/preparation-resources/sample-test-questions/general-training-test"),
        blocks = listOf(
            B.warmup(
                "ie5-types-w1", "ie5-types",
                "How many different kinds of letter do you think General Training Task 1 actually sets?",
                listOf("Dozens; it could be anything", "About five", "Only one, a complaint"), 1,
                "Few enough that an opening and a closing line can be learnt for each.",
            ),
            B.plain("Almost every General Task 1 is one of five letters. Learn an opening line, a middle structure and a closing line for each, and the twenty minutes become comfortable."),
            B.h("1 Complaint, formal"),
            B.table(
                listOf("Move", "Sentence to adapt"),
                listOf("Open", "I am writing to complain about the service I received at your Oxford Street branch on 14 March."),
                listOf("Explain", "I purchased a washing machine which was delivered three days later than the agreed date."),
                listOf("Impact", "As a result, I was forced to take a further day off work, and the machine was damaged on arrival."),
                listOf("Request", "I would therefore be grateful if you could arrange a replacement and refund the delivery charge."),
                listOf("Close", "I look forward to receiving your response within fourteen days."),
            ),
            B.h("2 Request or enquiry, formal"),
            B.table(
                listOf("Move", "Sentence to adapt"),
                listOf("Open", "I am writing to enquire about the evening courses advertised on your website."),
                listOf("Context", "I am currently working full time and am hoping to improve my qualifications."),
                listOf("Ask", "Could you please let me know whether places are still available, and what the fees include?"),
                listOf("Close", "Thank you for your assistance. I look forward to hearing from you."),
            ),
            B.h("3 Job or accommodation application, formal or semi-formal"),
            B.table(
                listOf("Move", "Sentence to adapt"),
                listOf("Open", "I am writing to apply for the position of part-time librarian advertised in the local paper."),
                listOf("Qualify", "I hold a degree in information management and have two years of relevant experience."),
                listOf("Availability", "I would be available to start from the beginning of June and can work weekends."),
                listOf("Close", "I have attached my curriculum vitae and would welcome the opportunity of an interview."),
            ),
            B.h("4 Apology or explanation, semi-formal"),
            B.table(
                listOf("Move", "Sentence to adapt"),
                listOf("Open", "I am writing to apologise for missing the meeting on Thursday afternoon."),
                listOf("Reason", "Unfortunately my train was cancelled and the replacement service arrived two hours late."),
                listOf("Repair", "I have read the minutes and have already completed the actions assigned to me."),
                listOf("Close", "Once again, please accept my apologies for any inconvenience caused."),
            ),
            B.h("5 Invitation, news or advice, informal"),
            B.table(
                listOf("Move", "Sentence to adapt"),
                listOf("Open", "Hi Anna, how are you? It feels like ages since we last spoke."),
                listOf("News", "I've finally moved into the new flat, and it's much closer to the park than the old one."),
                listOf("Invite or advise", "Why don't you come over on Saturday? If you can't make it, just let me know."),
                listOf("Close", "Write back soon and tell me your news. Best wishes, Usaid"),
            ),
            B.diagram(
                caption = "Every letter is the same shape. Only the tone and the middle content change.",
                nodes = listOf(
                    DNode("a", "why I am writing", 0.5f, 0.12f, w = 0.5f, h = 0.16f, tone = 1),
                    DNode("b", "bullet 1 with a detail", 0.5f, 0.38f, w = 0.5f, h = 0.16f, tone = 2),
                    DNode("c", "bullets 2 and 3", 0.5f, 0.64f, w = 0.5f, h = 0.16f, tone = 2),
                    DNode("d", "what should happen next", 0.5f, 0.88f, w = 0.5f, h = 0.16f, tone = 1),
                ),
                edges = listOf(DEdge("a", "b"), DEdge("b", "c"), DEdge("c", "d")),
                aspect = 0.62f,
            ),
            B.h("Polite structures that lift the band"),
            B.table(
                listOf("Instead of", "Write"),
                listOf("Send me a refund.", "I would be grateful if you could issue a refund."),
                listOf("I want to know the price.", "I would appreciate it if you could confirm the cost."),
                listOf("Tell me when it will arrive.", "Could you please let me know when I might expect delivery?"),
                listOf("You made a mistake.", "It appears that an error may have been made."),
                listOf("I am angry.", "I was extremely disappointed with the standard of service."),
            ),
            B.gap("ie5-t-g1", "Make it polite and formal.", "I would be ___ if you could send a replacement.", "grateful"),
            B.gap("ie5-t-g2", "Formal close after Dear Sir or Madam.", "Yours ___,", "faithfully"),
            B.gap("ie5-t-g3", "Informal close to a friend.", "Best ___, Usaid", "wishes", "regards"),
            B.check("ie5-types-k1", "ie5-types", "Which sentence suits a formal complaint?", listOf("Your delivery was rubbish and I want my money back.", "I was extremely disappointed with the delivery and would be grateful if you could refund the charge.", "Hi, the delivery didn't turn up, can you sort it?"), 1, "Formal letters state dissatisfaction politely and make a clear request."),
            B.check(
                "ie5-types-k2", "ie5-types",
                "A complaint letter has four moves. Which one do candidates most often leave out?",
                listOf("the request", "the explanation", "the opening"), 0,
                "People describe the problem at length and never say what they want done. The bullet usually asks for it in so many words.",
                whyWrong = mapOf(
                    1 to "The explanation is the part that writes itself; it is usually too long rather than missing.",
                    2 to "The opening line is the one sentence everybody remembers.",
                ),
            ),
            B.check(
                "ie5-types-k3", "ie5-types",
                "An enquiry letter's middle paragraph should contain",
                listOf("the specific questions you want answered", "your whole life story", "a complaint about the website"), 0,
                "An enquiry asks. Could you please let me know whether … and what … covers two bullets in one sentence.",
                whyWrong = mapOf(
                    1 to "Context is one sentence; the questions are the task.",
                    2 to "That is a different letter type, and answering the wrong one costs Task Achievement.",
                ),
            ),
            B.worked("""
                Worked example. A complaint, built from its four moves. Open with the purpose and a date: I am
                writing to complain about the service I received at your Oxford Street branch on 14 March. Explain
                what happened, with an invented but specific detail: I purchased a washing machine which was
                delivered three days later than the agreed date. Give the impact, which is what turns a description
                into a complaint: As a result, I was forced to take a further day off work. Then request something
                concrete: I would be grateful if you could arrange a replacement and refund the delivery charge.
            """),
            B.faded(
                "ie5-types-f1",
                "A different complaint, the same four moves: a hotel room that was noisy and not the one you booked.",
                listOf(
                    "Open with the purpose and a date: I am writing to complain about a room I stayed in at your Manchester hotel on 2 May.",
                    "Explain, with an invented detail: I had reserved a quiet double room at the rear of the building, but was given one overlooking the main road.",
                    "The move people leave out is the one that says what you want done. State it: I would be grateful if you could ___ the difference in rate and confirm the booking for my next stay.",
                    "Close looking forward: I look forward to receiving your response within fourteen days.",
                ),
                2, listOf("refund", "reimburse", "repay"),
                "Describing the problem is not complaining. The bullet nearly always asks what you want the company to do, and a letter that never says it has left a bullet uncovered.",
            ),
            B.mistakes(
                "Covering two bullets and forgetting the third; Task Achievement drops a whole band.",
                "Starting Dear Sir or Madam and signing Yours sincerely.",
                "Using contractions and phrasal verbs in a formal complaint.",
                "Writing 100 words because the bullets felt short; invent details and reach 150.",
            ),
            B.vocab(
                B.w("ie5-enquire", "enquire about", "ask for information about", "I am writing to enquire about the course.", "Formal alternative to ask."),
                B.w("ie5-grateful", "I would be grateful if", "a polite request formula", "I would be grateful if you could reply.", "Band 7 politeness."),
                B.w("ie5-inconvenience", "inconvenience", "trouble caused to someone", "Apologies for any inconvenience caused.", "Standard apology phrase."),
                B.w("ie5-refund", "refund", "money returned", "Please refund the delivery charge.", "Also a noun: a full refund."),
                B.w("ie5-cv", "curriculum vitae (CV)", "a record of your career", "I have attached my CV.", "Application letters."),
                B.w("ie5-make-it", "make it (to an event)", "manage to attend", "If you can't make it, let me know.", "Informal register."),
            ),
            B.tryIt("Try it in fifteen minutes", "The opening and closing line of each of the five letters, from memory.",
                "Complaint", "Enquiry", "Application", "Apology, and an invitation",
            ),
            B.explain(
                "ie5-types-x1",
                "Explain the four moves of a complaint letter, and which one candidates leave out.",
                "Open with the purpose and a date, so the reader knows what this is about. Explain what happened, with an invented but specific detail. Give the impact, which is what turns a description into a complaint. Then request something concrete: a replacement, a refund, an apology. The request is the move people drop, because describing the problem feels like the whole job, and the bullet nearly always asks what you want the company to do.",
                "Open, explain, impact, request" to listOf("impact", "request", "four moves"),
                "The request is what candidates leave out" to listOf("drop", "leave out", "people drop"),
                "The detail may be specific and invented" to listOf("invented", "specific detail"),
            ),
            B.glossary("Move" to "One job a sentence does in a letter: open, explain, request, close.", "Formula" to "A fixed polite phrase you can reuse.", "Bullet" to "One required content point."),
            B.recap("Five letter types cover nearly every task; learn an opening and closing for each.", "Polite formulas carry the formal register: I would be grateful if you could…", "Cover all three bullets, invent specifics, reach 150 words."),
        ),
    )

    private val practice = Topic(
        id = "ie5-practice", title = "Write two letters and have them marked", minutes = 22, references = refs + B.ref("Writing test resources and answer sheets", Ielts.ORG + "take-a-test/preparation-resources/writing-test-resources") + B.ref("General Training sample questions, paper by paper", Ielts.ORG + "take-a-test/preparation-resources/sample-test-questions/general-training-test"),
        blocks = listOf(
            B.warmup(
                "ie5-practice-w1", "ie5-practice",
                "The task gives three bullet points. You answer two of them very well and run out of time on the third. What do you think happens to Task Achievement?",
                listOf("Nothing, if the English is strong", "It drops a long way", "It rises, for depth"), 1,
                "A bullet is a part of the task, and a missing part is the heaviest penalty here.",
            ),
            B.plain("Two full tasks, one formal and one informal. The marker checks each bullet and the letter form: a greeting, a sign-off and the length. Write, read the marks, then rewrite before you look at the model."),
            B.diagram(
                caption = "What the marker checks in a letter, and what it cannot check. The second column is your job on the rewrite.",
                nodes = listOf(
                    DNode("a", "each bullet covered", 0.26f, 0.3f, w = 0.4f, h = 0.22f, tone = 1),
                    DNode("b", "greeting and sign-off", 0.26f, 0.7f, w = 0.4f, h = 0.22f, tone = 1),
                    DNode("c", "consistent tone", 0.74f, 0.3f, w = 0.4f, h = 0.22f, tone = 2, sub = "you check"),
                    DNode("d", "grammar range and accuracy", 0.74f, 0.7f, w = 0.4f, h = 0.22f, tone = 2, sub = "you check"),
                ),
                aspect = 0.7f,
            ),
            B.h("Task A: formal complaint"),
            B.p("You recently bought a mobile phone online, and it arrived with a cracked screen. Write a letter to the company. In your letter: explain what you bought and when; describe the problem; say what you want the company to do. Begin Dear Sir or Madam. Write at least 150 words."),
            B.marked("ie5-p-w1", "Write the formal complaint for Task A. Cover all three bullets, keep the tone formal, and use Dear Sir or Madam with Yours faithfully.",
                """
                Dear Sir or Madam,

                I am writing to complain about a mobile phone which I ordered from your online store on 3 September and received four days later.

                The device, a Model X7 in black, was advertised as new and was supplied in sealed packaging. However, when I opened the box I found that the screen was badly cracked across one corner, and the display flickers whenever the phone is switched on. The protective film was also torn, which suggests that the handset may have been damaged before it was dispatched.

                I would be grateful if you could arrange for the phone to be collected and replaced with a new unit at no cost to me. Should a replacement not be available, I would prefer a full refund of the purchase price of 320 pounds, together with the delivery charge.

                I look forward to receiving your response within fourteen days.

                Yours faithfully,
                Usaid Shahab
                """,
                "What you bought and when" to listOf("ordered", "bought", "purchased", "september", "received"),
                "The problem described" to listOf("cracked", "damaged", "broken", "screen"),
                "What you want done" to listOf("replace", "refund", "collected", "grateful if you could"),
                minWords = 150, form = WritingForm.LETTER),
            B.h("Task B: informal letter"),
            B.p("You are going to move to another city for work. Write a letter to an English-speaking friend. In your letter: tell them about the move; explain how you feel about it; invite them to visit. Write at least 150 words."),
            B.marked("ie5-p-w2", "Write the informal letter for Task B. Cover all three bullets and keep the tone friendly: contractions, questions and everyday vocabulary.",
                """
                Dear Anna,

                How are you? It feels like ages since we last caught up, and I've got some fairly big news to share with you.

                I'm moving to Hamburg at the end of next month. The company has offered me a position on their engineering team, and after thinking it over for a couple of weeks I decided I couldn't turn it down. I've already found a small flat near the harbour, which is only twenty minutes from the office.

                To be honest, I'm feeling a bit of everything. I'm excited about the job and about living somewhere new, but I'm also nervous about my German and I'll miss everyone here more than I like to admit.

                Anyway, why don't you come and stay once I've unpacked? There's a spare room with your name on it, and I'd love to show you the city. Let me know which weekends suit you.

                Write back soon.

                Best wishes,
                Usaid
                """,
                "Tell them about the move" to listOf("moving", "move", "relocat", "new job", "position"),
                "How you feel about it" to listOf("excited", "nervous", "feel", "happy", "sad", "worried", "miss"),
                "Invite them to visit" to listOf("come and stay", "come over", "visit", "spare room", "why don't you"),
                minWords = 150, form = WritingForm.LETTER),
            B.tryIt("After the marker", "Check the three things the marker cannot see.",
                "Read it aloud: does the tone stay the same from greeting to sign-off?", "Count your complex sentences: aim for at least four with although, which, if or because", "Replace two basic words with precise ones: nice, good, bad, thing", "Check every bullet has a specific detail, not a vague sentence", "Time it: 20 minutes including two minutes of planning",
            ),
            B.check("ie5-practice-k1", "ie5-practice", "The marker says the sign-off is missing from your formal letter. You should add", listOf("Thanks!", "Yours faithfully, and your name", "The End"), 1, "A formal letter to an unnamed recipient closes with Yours faithfully and your full name."),
            B.check(
                "ie5-practice-k2", "ie5-practice",
                "You cover two of the three bullets very well and run out of time on the third. Task Achievement",
                listOf("drops a long way", "is unaffected if the English is strong", "rises, for depth"), 0,
                "A bullet is a part of the task, and an uncovered part is the heaviest single penalty in this paper.",
                whyWrong = mapOf(
                    1 to "Task Achievement asks whether you did what was asked, not how well you wrote the rest.",
                    2 to "Depth on two bullets does not buy the third.",
                ),
            ),
            B.check(
                "ie5-practice-k3", "ie5-practice",
                "After the marker reports back, the step that moves the band is",
                listOf("rewriting this letter with the gaps filled", "writing a new letter", "reading the model answer first"), 0,
                "The rewrite is where the correction is practised. Reading the model before your own attempt replaces your plan with somebody else's.",
                whyWrong = mapOf(
                    1 to "A new letter repeats whatever habit produced the gap.",
                    2 to "The model is read last, once your own version exists to compare it against.",
                ),
            ),
            B.worked("""
                Worked example. How the twenty minutes go. Two minutes deciding the tone and therefore the greeting
                and sign-off. One minute turning the three bullets into three paragraph plans. Fourteen minutes
                writing, one bullet at a time, inventing any detail the bullet needs. Three minutes checking: are all
                three bullets there, is the length over 150 words, and do the greeting and sign-off match.
            """),
            B.faded(
                "ie5-practice-f1",
                "You open Task 1 and it is an informal letter to a friend who has invited you to stay. Plan the twenty minutes before writing a word.",
                listOf(
                    "Two minutes on tone. A friend takes Dear Anna, or Hi Anna, and contractions are not only allowed but expected.",
                    "One minute turning the three bullets into three paragraph plans.",
                    "Fourteen minutes writing, and any detail a bullet needs may simply be ___ rather than remembered.",
                    "Three minutes checking: all three bullets covered, over 150 words, greeting and sign-off matching.",
                ),
                2, listOf("invented", "made up"),
                "Nothing in Task 1 is fact-checked. Candidates lose marks hesitating over a detail they were always free to supply.",
            ),
            B.vocab(
                B.w("ie5-dispatch", "dispatch", "send out goods", "Damaged before it was dispatched.", "Formal commercial verb."),
                B.w("ie5-handset", "handset", "a mobile phone", "The handset was faulty.", "Formal alternative."),
                B.w("ie5-turn-down", "turn down", "refuse", "I couldn't turn it down.", "Informal phrasal verb."),
                B.w("ie5-catch-up", "catch up", "meet and exchange news", "It's ages since we caught up.", "Informal opener."),
                B.w("ie5-to-be-honest", "to be honest", "frankly", "To be honest, I'm nervous.", "Informal discourse marker."),
                B.w("ie5-ages", "ages", "a long time", "It feels like ages.", "Informal only."),
            ),
            B.mistakes(
                "Covering two bullets beautifully and running out of time on the third.",
                "Hesitating over a detail the task did not supply. Invent it.",
                "Letting the greeting and the sign-off drift apart as the letter gets longer.",
                "Writing a new letter instead of rewriting the marked one.",
            ),
            B.explain(
                "ie5-practice-x1",
                "Explain what happens to Task Achievement when a bullet is left uncovered, and what to do after a letter is marked.",
                "Each of the three bullets is marked on its own, so covering two beautifully and running out of time on the third drops Task Achievement a long way; depth on two does not buy the third. After marking, the step that moves the band is rewriting this letter with the gaps filled, not writing a new one on a new prompt, because a new prompt lets the same habit repeat on fresh ground. The model answer is read last.",
                "Every bullet is marked separately" to listOf("marked on its own", "separately", "each bullet"),
                "An uncovered bullet costs Task Achievement heavily" to listOf("task achievement", "drops", "heaviest"),
                "Rewrite the marked letter rather than start a new one" to listOf("rewriting", "rewrite", "new one"),
            ),
            B.glossary("Model answer" to "A band 8 example to compare against after you write.", "Rewrite" to "Your second draft after the marks.", "Discourse marker" to "A phrase that signals the next move: anyway, to be honest."),
            B.recap("Write, mark, rewrite; only then read the model.", "The marker checks bullets, greeting, sign-off and length.", "You check tone consistency, complex sentences and precise vocabulary."),
        ),
    )

    val module = Module(
        id = "ie-m5", number = 5, title = "Writing Task 1: General", summary = "The letter: formal, semi-formal and informal tone, five recurring types, two marked tasks.",
        examWeight = "about 8 percent", componentId = "writing", topics = listOf(tone, types, practice), practiceMinutes = 420,
        quiz = Quiz(
            id = "ie-m5-test", title = "Module 5 test", passPercent = 70,
            questions = B.spread(listOf(
                B.q(
                    "ie5-q1", "ie5-tone", "A letter beginning Dear Sir or Madam should end",
                    listOf("Yours sincerely", "Yours faithfully", "Best wishes"), setOf(1),
                    "No name means Yours faithfully; a name means Yours sincerely.",
                    whyWrong = mapOf(
                        0 to "Sincerely goes with a name. You have not used one, so faithfully is the pair.",
                        2 to "Best wishes is for someone you know. It breaks the register of a formal letter.",
                    ),
                ),
                B.q(
                    "ie5-q2", "ie5-tone", "A letter beginning Dear Mrs Patel should end",
                    listOf("Yours faithfully", "Yours sincerely", "Love"), setOf(1),
                    "You have used her name, so sincerely is correct.",
                    whyWrong = mapOf(
                        0 to "Faithfully is for a letter with no name in the greeting. Here there is one.",
                        2 to "Love is for family and close friends, never for a surname and a title.",
                    ),
                ),
                B.q(
                    "ie5-q3", "ie5-tone", "Contractions such as I'm and don't belong in",
                    listOf("formal letters", "informal letters", "all letters"), setOf(1),
                    "Write them out in formal and semi-formal letters.",
                    whyWrong = mapOf(
                        0 to "A formal letter writes them out in full. Contractions are one of the quickest ways to drop the register.",
                        2 to "They are fine in one of the three tones and wrong in the other two, which is the whole point of choosing a tone.",
                    ),
                ),
                B.q(
                    "ie5-q4", "ie5-tone", "The task gives three bullet points. You cover two in detail and skip one. The likely effect is",
                    listOf("no effect if the English is good", "Task Achievement drops substantially", "a higher score for depth"), setOf(1),
                    "All three must be covered; missing one is penalised heavily.",
                    whyWrong = mapOf(
                        0 to "Good English is three of the four criteria. The fourth asks whether you did the task, and you did two thirds of it.",
                        2 to "Depth on two bullets does not buy the third. Cover all three, briefly if need be.",
                    ),
                ),
                B.q(
                    "ie5-q5", "ie5-tone", "Inventing a date, price or flat number to cover a bullet is",
                    listOf("cheating and penalised", "expected and sensible", "only allowed in informal letters"), setOf(1),
                    "Examiners do not check facts; specifics make a bullet convincing.",
                    whyWrong = mapOf(
                        0 to "Nothing in the letter is verifiable, and nobody is checking. Vagueness costs marks; invention does not.",
                        2 to "The tone of the letter has nothing to do with it. A formal complaint needs a date as much as a note to a friend does.",
                    ),
                ),
                B.q(
                    "ie5-q6", "ie5-types", "Which is the most appropriate formal request?",
                    listOf("Send me the details.", "I would be grateful if you could send me further details.", "Can you send the details please?"), setOf(1),
                    "The grateful formula is the standard polite request.",
                    whyWrong = mapOf(
                        0 to "A bare imperative reads as an order. It is grammatical and still wrong for the register.",
                        2 to "'Can you … please' is polite enough for a colleague, but it is semi-formal at best.",
                    ),
                ),
                B.q(
                    "ie5-q7", "ie5-types", "'Put up with' and 'sort out' are",
                    listOf("formal verbs", "phrasal verbs better suited to informal letters", "incorrect English"), setOf(1),
                    "Prefer tolerate and resolve in a formal letter.",
                    whyWrong = mapOf(
                        0 to "Phrasal verbs are the informal half of most pairs. Their one-word equivalents are the formal half.",
                        2 to "Both are perfectly good English. The question is where they belong, not whether they are right.",
                    ),
                ),
                B.q(
                    "ie5-q8", "ie5-types", "A letter apologising for missing a meeting should include",
                    listOf("only the apology", "the apology, a reason and what you have done about it", "a complaint about the organiser"), setOf(1),
                    "Open, reason, repair, close is the standard shape.",
                    whyWrong = mapOf(
                        0 to "An apology alone will not fill three bullets or 150 words, and the task always asks for more than one thing.",
                        2 to "Turning an apology into a complaint answers a different task from the one set.",
                    ),
                ),
                B.q(
                    "ie5-q9", "ie5-types", "Which opening suits an enquiry about a course?",
                    listOf("I am writing to enquire about the evening courses advertised on your website.", "Tell me about your courses.", "Hi there, got any courses?"), setOf(0),
                    "Formal, specific and states the purpose in one sentence.",
                    whyWrong = mapOf(
                        1 to "It states the purpose but reads as an instruction, and it names nothing specific.",
                        2 to "Informal greeting, contraction and a dropped subject: three register breaks in six words.",
                    ),
                ),
                B.q(
                    "ie5-q10", "ie5-practice", "The minimum length for General Task 1 is",
                    listOf("100 words", "150 words", "250 words"), setOf(1),
                    "Under-length answers are penalised automatically.",
                    whyWrong = mapOf(
                        0 to "A hundred words is under length, and the penalty applies before the content is weighed.",
                        2 to "250 is Task 2. A letter that long is eating the time the essay needs.",
                    ),
                ),
                B.q(
                    "ie5-q11", "ie5-practice", "How long should Task 1 take?",
                    listOf("20 minutes", "40 minutes", "60 minutes"), setOf(0),
                    "Task 2 is worth twice as much and needs the remaining forty.",
                    whyWrong = mapOf(
                        1 to "Forty is Task 2's share. Spending it here leaves twenty for the essay that carries two thirds of the mark.",
                        2 to "An hour covers both tasks, not one.",
                    ),
                ),
                B.q(
                    "ie5-q12", "ie5-tone", "Which phrase is too informal for a complaint to a company?",
                    listOf("I was extremely disappointed", "Your service was rubbish", "The standard fell below what was advertised"), setOf(1),
                    "Strong informal vocabulary breaks the register.",
                    whyWrong = mapOf(
                        0 to "Strong feeling is fine in a formal complaint as long as the words are formal, and 'extremely disappointed' is.",
                        2 to "This is the most formal of the three: measured, specific and impersonal.",
                    ),
                ),
            )),
        ),
    )
}
