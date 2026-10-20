package com.shahaabapps.core.content

/** The Security module's identity platform and MSAL topics as Case Files and Gossip, with their boards (Az204StyledPodcasts). */
internal object Az204StyledSecurity2 {
    private val identityCase = Podcast(
        "az-identity",
        listOf(
            maya("Welcome to Case Files. Today's case: The Borrowed Ticket. A small gym runs a class booking app for Android. Members sign in, book a spin class, and the app calls the gym's own API on Azure App Service. On Monday morning, twelve members found their bookings cancelled. None of them had touched a thing. And the API's logs say it accepted every one of those calls."),
            leo("[chuckle] A mystery at the gym. Detective Leo, warming up. So how does the API decide who's allowed in?"),
            maya("With tokens. Members don't sign in to the gym itself. They sign in with the Microsoft identity platform, the sign-in service behind Microsoft accounts and Entra ID, which is the new name for Azure Active Directory. Think of a hotel front desk. The member goes to the desk, proves who they are, and comes back with a token as proof."),
            leo("And what is a token, really? A paper ticket?"),
            maya("Under the picture, it's a short piece of signed text, called a JWT. It carries claims, little facts like who the user is, which API it was made for, and when it runs out. Microsoft signs it, so nobody can change a claim without breaking the signature. An ID token says who the user is. An access token says what the caller may do on an API."),
            leo("Then my first suspect is a stolen secret. Someone pulled the client secret out of the Android app and made their own tokens."),
            maya("[pause] Good instinct, but check the registration. The gym app is a public client: a mobile app, which can't keep a secret, because anything on a phone can be taken apart. So it has no secret at all, rightly. It signs members in with the authorization code flow and PKCE, Proof Key for Code Exchange."),
            leo("PKCE. Remind me, in plain words?"),
            maya("A cloakroom ticket with a secret word. Here's how it actually works. Before sign-in, the app makes up a random word and sends only a scrambled version of it. Sign-in hands back a one-time code. To swap that code for tokens, the app must send the original word. A thief who copies the code on the way has no word, so the code is useless."),
            leo("Fine, nothing to steal. Suspect two. Somebody gave the app application permissions. Far too much power."),
            maya("Let's look. There are two kinds of permission, and the question is whether a person is there. A delegated permission means a signed-in user acts through the app, like a friend with your key, who can only do what you could do. It shows up in the token as the scp claim. Booking your own class is delegated."),
            leo("And an application permission?"),
            maya("The app acts as itself, with no user at all. It shows up as the roles claim, and an admin always has to consent. The gym has one: a nightly report that counts every booking, using client credentials and a certificate. The Key Vault episode covers keeping that safe. But the Android app asks for delegated only, as it should."),
            leo("So no stolen secret, no giant permission. [pause] Then the token itself was fake?"),
            maya("That's the clue. The token was real, properly signed by Microsoft. But look at one claim, the audience, aud for short. It says which API the token was made for. This token's audience was Microsoft Graph, not the gym's API. Someone signed in to a little app of their own, got a token meant for Graph, and sent it to the gym."),
            leo("Someone else's concert ticket, at our door. And the door staff let it in?"),
            maya("They checked that the ticket was printed properly and stopped there. That's the mistake. An API must check the audience on every access token. A token for Graph is not a token for you. The fix was a few lines in the API: accept only tokens whose audience is the gym API's own id."),
            leo("Case closed, then?"),
            maya("One more clue in the file. The Android app decided whether to show the trainer's cancel button by reading a claim in the access token. When Microsoft changed that token's format, the button vanished for every trainer."),
            leo("[sigh] And that's wrong because the access token isn't for the app."),
            maya("Right. The access token is for the API, and the app should treat it as sealed. The app reads the ID token for the member's name, and asks the API what the member may do. The phone shows, the server decides. And the third kind, the refresh token, just gets new tokens quietly later."),
            leo("Let's back up. How does Microsoft even know about the gym app?"),
            maya("You register it, once. That creates an application object, the single record of your app in its home tenant. A tenant is one organisation's own space in Entra ID. It holds the client id, who may sign in, the redirect URI where tokens come back, and the API permissions it wants. The API's own registration lists its scopes under Expose an API, like a Bookings read-write scope."),
            leo("And when another gym chain wants to use the app?"),
            maya("Make it multitenant. When their admin consents, their tenant gets a service principal, your app's visitor pass inside their building. Permissions and roles attach to the service principal, not the application object. So, one application object, and one service principal in each tenant that uses it."),
            leo("Where does this idea stop working? Like the gym's TV screen with the class list?"),
            maya("That's the edge. A TV has no browser, so it uses device code: the screen shows a short code, and someone types it on their phone. An API calling another API as the same member uses on-behalf-of. And implicit, an old flow for web pages, is deprecated. The MSAL episode shows how the Android app asks for its tokens in code."),
            leo("So, the case summary for the exam?"),
            maya("Five rules. If nobody is signed in, like a nightly job, the answer is an application permission, admin consent and client credentials. If a user works with their own data, delegated. If it's a mobile or single-page app, authorization code with PKCE, never implicit and never a secret."),
            leo("And the other two?"),
            maya("If another company's tenant uses your app, a service principal is created there, and permissions attach to it. And if the question asks what an API must check on every token, it's the audience. That one cracked our case."),
            leo("Case closed. Anything to try today?"),
            maya("Any free Entra tenant works. Register an app with a localhost redirect URI. Note the client id and the tenant id. Add Microsoft Graph's User Read under API permissions. Then open Authentication, and compare the public client and web platform settings. That's the gym app's registration, built by you."),
            leo("And nobody cancels my spin class with a borrowed ticket. See you next time."),
        ),
        style = PodcastStyle.CASE_FILES,
    )

    private val identityGossip = Podcast(
        "az-identity",
        listOf(
            maya("Okay, did you hear what Sam did with the bike rental app? Grab a coffee. It's the one where you scan a bike in the street with your Android phone, and the app calls a small API on Azure to unlock it. Sam is lovely, by the way, and will happily tell you this story too."),
            leo("[laugh] I heard the words client secret and app store in the same sentence. What did Sam do?"),
            maya("Sam registered the app with the Microsoft identity platform, which is good. That's the sign-in service behind Microsoft accounts and Entra ID, the new name for Azure Active Directory. Then Sam wanted the app to prove who it was. So Sam made a client secret, and put it inside the Android app."),
            leo("And shipped it to everyone's phone."),
            maya("Twenty thousand phones. Aisha from security pulled it out of the app package over lunch. Anything on a user's device can be taken apart, so a mobile app is a public client: it can't keep a secret. Only a confidential client, a web app or a job on a server, holds a secret or a certificate."),
            leo("So how does a phone app prove anything without a secret?"),
            maya("Aisha's fix was the authorization code flow with PKCE, Proof Key for Code Exchange. Picture a cloakroom ticket plus a secret word you made up. Here's how it actually works. The app invents a random word for each sign-in and sends a scrambled version first. To swap the sign-in code for tokens, it must send the original word. A copied code alone is useless."),
            leo("And the word changes every time, so there's nothing to steal from the app package. Nice. Who's next?"),
            maya("Ben. Ben built the premium features. Premium riders get e-bikes. Ben's Android app read the access token, found a claim in it, and used that to decide whether to show the e-bike button."),
            leo("That sounds clever. Is it bad?"),
            maya("It's opening someone else's post. Under the picture, a token is a short signed text, a JWT, full of claims, little facts. The ID token says who the user is, and it's for the app. The access token says what the caller may do, and it's for the API. Its format can change, and when it did, Ben's button vanished for everyone."),
            leo("[chuckle] So all the premium riders were walking. What should Ben have done?"),
            maya("Read the ID token for the rider's name, and ask the API whether this rider is premium. The API decides. And the refresh token, the third kind, just lets the app get new tokens quietly, without asking the rider to sign in again."),
            leo("Who's next? I heard the night job had a meltdown."),
            maya("That was Nadia, and it's the opposite mix-up. She was building two things. A nightly job that reads every bike's battery report, and the rider sign-in. She gave the rider sign-in application permissions, because they sounded stronger, and she figured stronger was safer."),
            leo("And what happened?"),
            maya("Riders hit a screen saying an admin must approve the app. An application permission means the app acts as itself, with no user, and an admin always has to consent. It arrives in the token as the roles claim. Riders working with their own rentals need a delegated permission, which acts on behalf of the signed-in user and arrives as the scp claim."),
            leo("So the night job gets the application permission, and the riders get delegated."),
            maya("Exactly. Nadia's job now uses client credentials, the flow for no user, with a certificate. The Key Vault episode covers keeping that certificate safe. Is a person there? That one question picks the kind."),
            leo("Any more? This office is a disaster."),
            maya("[pause] One more, and it's the scary one. Ravi on the API team checked that each token was properly signed by Microsoft, and stopped there. He never checked the audience, aud for short, the claim that says which API the token was made for."),
            leo("Does that matter, if Microsoft signed it?"),
            maya("A tester signed in to a little app of her own, got a token for Microsoft Graph, and sent it to the bike API. It unlocked a bike. Someone else's concert ticket, and the door staff only checked it was printed properly. A token for Graph is not a token for you. Ravi added the audience check that afternoon, so the API accepts only its own tokens."),
            leo("How does the bike API get an audience of its own, anyway?"),
            maya("By being registered. Registration makes an application object, the single record of an app in its home tenant, the company's own space in Entra ID. It holds the client id, the redirect URI where tokens come back, and the API permissions it wants. The API lists its scopes under Expose an API, like a Rides read-write scope, and its id becomes the audience."),
            leo("And I heard a city council wants the app for their staff."),
            maya("That's the happy gossip. Make the app multitenant. When the council's admin consents, their tenant gets a service principal, your app's visitor pass in their building. Permissions and roles attach to it. One application object, and one service principal per tenant."),
            leo("Where does all this stop working? Like the docking station screens?"),
            maya("A screen with no browser uses device code: it shows a short code, and you type it on your phone. An API calling another API as the same rider uses on-behalf-of. And implicit, an old flow for web pages, is deprecated. The MSAL episode shows the Android side in code."),
            leo("So, the gossip I should remember for the exam?"),
            maya("Five things. Nobody signed in, like a nightly job, means an application permission, admin consent and client credentials. A user with their own data means delegated. A mobile or single-page app means authorization code with PKCE, never implicit, never a secret. Another company's tenant gets a service principal. And an API always checks the audience."),
            leo("And something to try today, so I'm not next week's story?"),
            maya("Any free Entra tenant works. Register an app with a localhost redirect URI. Note the client id and the tenant id. Add Microsoft Graph's User Read under API permissions. Then open Authentication, and compare the public client and web platform settings."),
            leo("No secrets in phone apps. Got it, Sam. See you next time."),
        ),
        style = PodcastStyle.GOSSIP,
    )

    private val msalCase = Podcast(
        "az-msal",
        listOf(
            maya("Welcome to Case Files. Today's case: The Endless Sign-in. A small plant watering app for Android reminds you when your ferns are thirsty, and syncs your plants through its own API on Azure. Since last week's update, every time someone opens the app, it shows the Microsoft sign-in window. Every single time. The reviews are brutal."),
            leo("[chuckle] Even the ferns are leaving. Detective Leo reporting. Where do we start?"),
            maya("With the tool. The app gets its tokens through MSAL, the Microsoft Authentication Library. You tell it which app you are and which scopes you need, a scope being one named permission. MSAL shows the sign-in window, keeps tokens in a cache, and renews them when they run out."),
            leo("And what is it really, under the picture?"),
            maya("A library inside your app, with versions for Android, dot NET, JavaScript and more. It runs the Microsoft identity platform's sign-in flows for you, and keeps accounts and tokens in a cache on the device. On Android it does the sign-in in a browser, then catches the answer on a special redirect built from the app's package name and signature."),
            leo("Then my first suspect: the tokens expire too fast. Someone set them to last a minute."),
            maya("[pause] Good thought, but an access token normally lasts around an hour, and that's fine. When one expires, MSAL uses the refresh token to fetch a new one, quietly, with no window. Someone who opened the app a month ago should still glide straight in."),
            leo("Suspect two, then. The cache is broken. MSAL forgot everyone."),
            maya("Closer. Let's read the code. On launch, the update calls AcquireTokenInteractive, straight away, every time. That's the method that shows the sign-in window. Nobody ever asks the cache."),
            leo("So the cache is fine. It's just never asked."),
            maya("Exactly. The right pattern is silent first. Call AcquireTokenSilent. Here's how it actually works. It looks in the cache for a valid access token for that account and those scopes. If the token has expired, it sends the refresh token to the identity platform and gets new tokens back, all with no prompt."),
            leo("And if silent can't help?"),
            maya("Then it throws MsalUiRequiredException. The name does the work: UI means user interface. The user has to be asked something. Maybe they must consent to a new permission, do a second check on their phone, or their session has expired. Only then do you call AcquireTokenInteractive."),
            leo("Couldn't I just retry silently a few times?"),
            maya("No. Retrying can't change what's in the cache, so it throws again. And building a new client object doesn't help, because it shares the same cache. Silent first, interactive only on that exception."),
            leo("So on the very first run, what should happen?"),
            maya("Silent throws, because the cache is empty. So the app shows the Microsoft sign-in once and saves the account. From then on silent works for months, because the refresh token keeps fetching new access tokens. That's exactly what the app did before the update. Case cracked, mostly."),
            leo("Mostly? [sigh] There's more, isn't there."),
            maya("Two more witnesses. The developer behind the update had a theory. If the app were a confidential client, with a client secret inside, it could get tokens without bothering the user at all."),
            leo("Is that not a good idea?"),
            maya("It's the classic mistake. MSAL has two builders, the starting point in code. The public client builder is for apps on a user's device: mobile, desktop, command line. The confidential client builder is for servers: web apps, APIs and background jobs. A phone can be taken apart, so a phone app is a public client, with no secret in it. Ever."),
            leo("So where does a secret belong?"),
            maya("On a server, and even then, in Key Vault. The Key Vault episode covers that side. Say the plant app has a nightly job that emails watering reports. No user is there, so it's client credentials, the app signing in as itself. On a confidential client it calls AcquireTokenForClient, with the resource's address and slash dot default as the scope."),
            leo("Dot default, meaning what?"),
            maya("Give me every permission already consented for this app on that resource. With no user, nobody can consent to anything narrower, so client credentials always uses dot default. Asking for User Read there just fails."),
            leo("And the second witness?"),
            maya("The profile photo. The app shows your photo from Microsoft Graph next to your plants. The update asked for Graph's User Read and the plant API's scope in one call, to save time. And it failed."),
            leo("Why? The app has both permissions."),
            maya("Because every access token has one audience, the one API it was made for. Like a train ticket for one line. So you ask for Graph scopes in one call and the plant API's scope in another. The Microsoft Graph episode covers the photo itself."),
            leo("I've also seen openid and profile in the sign-in code. What are those?"),
            maya("Sign-in scopes from OpenID Connect, the standard for proving who someone is. And offline access is the scope that gets you a refresh token. That's the one that lets silent keep working for months."),
            leo("Where does all this stop working? Like the plant shop's web dashboard?"),
            maya("Good edge. The dashboard is a web app on three servers. It signs people in through Microsoft Identity Web, which wraps MSAL for dot NET. But it kept tokens in each server's memory, so people lost them whenever a request hit another server. Use a shared cache, like Redis or SQL. The Redis cache episode has that."),
            leo("Any other edges?"),
            maya("A command line tool with no browser uses AcquireTokenWithDeviceCode, and you type a code on your phone. An API calling another API as the same user uses AcquireTokenOnBehalfOf. And a web app signing users in uses AcquireTokenByAuthorizationCode."),
            leo("So, the case summary for the exam?"),
            maya("Four rules. If users complain about constant sign-in prompts, call AcquireTokenSilent first, and interactive only on MsalUiRequiredException. If a daemon with no user needs a token, AcquireTokenForClient with the dot default scope. If one request mixes two APIs and fails, a token has one audience. And if there's no browser, device code."),
            leo("Case closed. Anything to try today?"),
            maya("Use the app registration from the identity platform episode. Create a dot NET console app and add the MSAL package. Acquire a token for User Read interactively. Paste the access token into a token decoder and read its audience and scope claims. Then run it again, and confirm the silent path succeeds."),
            leo("Silent, like a well-watered fern. See you next time."),
        ),
        style = PodcastStyle.CASE_FILES,
    )

    private val msalGossip = Podcast(
        "az-msal",
        listOf(
            maya("Okay, did you hear what Ben did to the quiz app? You know, the Android trivia app with the weekly leaderboard, and a small API on Azure keeping the scores."),
            leo("[laugh] I heard people were signing in more often than answering questions. What happened?"),
            maya("Pretty much that. Ben added sign-in with MSAL, the Microsoft Authentication Library. It talks to the Microsoft sign-in service for you. You say which app you are and which scopes you need, a scope being one named permission. It shows the sign-in window, keeps tokens in a cache, and renews them when they run out, so nobody builds a token request by hand."),
            leo("And Ben?"),
            maya("Ben called AcquireTokenInteractive before every request. Every answer you submitted, the sign-in window popped up. Players lasted about three questions, and the store reviews were not kind."),
            leo("[sigh] I'd last two. Who fixed it?"),
            maya("Priya. She said, silent first. Call AcquireTokenSilent. Here's how it actually works. It looks in the cache for a valid access token for that player. If it has expired, it sends the refresh token to the identity platform and gets a fresh one back, with no window. Like a coffee shop loyalty card. They know you, so no forms."),
            leo("And when silent can't manage it?"),
            maya("It throws MsalUiRequiredException. UI means user interface: the player has to be asked something. Maybe consent to a new permission, a second check on their phone, or a session that's expired. Only then do you call interactive. Retrying silently just throws again, because nothing in the cache has changed."),
            leo("So how long does silent keep a player signed in?"),
            maya("For months. On the very first run, silent throws, because the cache is empty. So the app shows the Microsoft sign-in once and saves the account. After that, a player who comes back weeks later still glides straight in, because the refresh token fetches a new access token."),
            leo("What is MSAL, really, under the loyalty card?"),
            maya("A library inside your app, with versions for Android, dot NET, JavaScript and others. It runs the identity platform's sign-in flows, keeps accounts and tokens in a cache on the device, and on Android does the sign-in in a browser and catches the answer on a redirect built from the app's package name. The identity platform episode covers the flows themselves."),
            leo("Okay, who's next?"),
            maya("The intern, bless them. They read that confidential clients get tokens without any prompt, which is true, on a server. So they switched the Android app to the confidential client builder, and put a client secret inside it."),
            leo("Inside the app on everyone's phone. Even I know that one."),
            maya("Priya caught it in review. MSAL has two builders. The public client builder is for apps on a user's device: mobile, desktop and command line. The confidential client builder is for servers: web apps, APIs and background jobs. A phone app can be taken apart, so it's always public, and never holds a secret. Server secrets live in Key Vault, which has its own episode."),
            leo("Who's next?"),
            maya("Aisha. She wanted each player's Microsoft profile photo on the leaderboard. So in one call she asked for Graph's User Read scope and the quiz API's scope together."),
            leo("Efficient. Did it work?"),
            maya("It failed. Every access token has one audience, meaning the one API it was made for. Like a train ticket for one line only. So Graph scopes go in one call, and the quiz API's scopes in another. Scopes are written as the resource, then the permission. The Microsoft Graph episode covers the photo part."),
            leo("I've also seen openid and profile in sign-in code. What are those?"),
            maya("Sign-in scopes from OpenID Connect, the standard for proving who someone is. And offline access is the scope that gets you a refresh token, which is what keeps Priya's silent path working for months."),
            leo("[chuckle] Noted. And I heard about a leaderboard job?"),
            maya("That was Felix. Every Sunday night, a job crowns the weekly winners. No player is signed in. Felix tried AcquireTokenSilent there, and when that threw, asked for User Read. Neither works with nobody around, because there's no player to sign in or to consent."),
            leo("So what does a job with no user do?"),
            maya("Client credentials: the app signs in as itself. On the confidential client builder, Felix calls AcquireTokenForClient, and the scope is the resource's address followed by slash dot default. That means every permission already consented for the app on that resource. With no user, nobody can consent to anything narrower."),
            leo("And the last one? There's always a last one."),
            maya("Tess runs the quiz website, the web version of the leaderboard, on three servers. Players kept getting signed out at random. She'd kept the tokens in each server's memory."),
            leo("So the next request lands on another server, and the tokens aren't there."),
            maya("Exactly. Memory caches vanish on scale-out. Tess moved to Microsoft Identity Web, which wraps MSAL for dot NET web apps and signs users in with AcquireTokenByAuthorizationCode, and she keeps the tokens in a shared cache, Redis or SQL. The Redis cache episode covers that part."),
            leo("Where does all this stop working? Like the quiz night screen at the pub?"),
            maya("That's the edge. A screen or a command line tool with no browser uses AcquireTokenWithDeviceCode: it shows a code, and you type it on your phone. And an API that calls another API as the same player uses AcquireTokenOnBehalfOf."),
            leo("So, the gossip I should remember for the exam?"),
            maya("Four things. Constant sign-in prompts means AcquireTokenSilent first, and interactive only on MsalUiRequiredException. A daemon with no user means AcquireTokenForClient with the dot default scope. Two APIs in one request failing means a token has one audience. And no browser means device code. Public builder for apps with users, confidential for servers."),
            leo("And something to try today, so I'm not the next story?"),
            maya("Use the app registration from the identity platform episode. Create a dot NET console app and add the MSAL package. Acquire a token for User Read interactively. Paste the access token into a token decoder and read its audience and scope claims. Then run it again, and confirm the silent path succeeds."),
            leo("Silent first, Ben. See you next time."),
        ),
        style = PodcastStyle.GOSSIP,
    )

    val podcasts: List<Podcast> = listOf(identityCase, identityGossip, msalCase, msalGossip)

    private fun identityCaseBoard() = PodcastBoard(
        "az-identity--case-files",
        listOf(
            // 0: "Welcome to Case Files. Today's case: The Borrowed Ticket..."
            Scene(
                0, "The Borrowed Ticket",
                DiagramSpec(
                    "The gym's Android app signs members in with Microsoft and calls the gym's API, which accepted twelve cancellations nobody made.",
                    nodes = listOf(
                        DNode("phone", "Gym app", 0.22f, 0.2f, w = 0.36f, h = 0.18f, sub = "Android"),
                        DNode("api", "Gym API", 0.22f, 0.66f, w = 0.32f, h = 0.2f, sub = "App Service"),
                        DNode("err", "12 bookings cancelled", 0.76f, 0.2f, w = 0.42f, h = 0.18f, kind = NodeKind.NOTE, tone = 2),
                        DNode("idp", "Microsoft sign-in", 0.76f, 0.66f, w = 0.42f, h = 0.3f, kind = NodeKind.CLOUD),
                    ),
                    edges = listOf(
                        DEdge("phone", "api", "books"),
                        DEdge("phone", "idp", "signs in"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(0, show = setOf("phone", "api", "err"), light = setOf("err"), pulse = "phone" to "api", card = BoardCard("The case", "Monday: twelve bookings cancelled, and the API accepted every call.")),
                    Beat(2, show = setOf("idp"), light = setOf("idp"), pulse = "phone" to "idp", card = BoardCard("Entra ID", "The new name for Azure Active Directory. Microsoft signs members in.")),
                    Beat(4, light = setOf("idp", "phone"), card = BoardCard("A token, under the picture", "A JWT: signed text full of claims. Change one and the signature breaks.")),
                ),
            ),
            // 5: "Then my first suspect is a stolen secret..."
            Scene(
                5, "Suspect one: a secret",
                DiagramSpec(
                    "A phone app is a public client with no secret; PKCE makes a copied sign-in code useless without the app's secret word.",
                    nodes = listOf(
                        DNode("pub", "Public client", 0.26f, 0.2f, w = 0.44f, h = 0.22f, sub = "the Android app"),
                        DNode("sec", "Client secret?", 0.75f, 0.2f, w = 0.44f, h = 0.22f, kind = NodeKind.NOTE, tone = 2),
                        DNode("pkce", "PKCE", 0.26f, 0.65f, w = 0.34f, kind = NodeKind.PILL, sub = "ticket + secret word"),
                        DNode("code", "One-time code", 0.75f, 0.65f, w = 0.44f, kind = NodeKind.PILL, tone = 1, sub = "useless alone"),
                    ),
                    edges = listOf(
                        DEdge("pub", "pkce"),
                        DEdge("pkce", "code", "swapped with the word"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(5, show = setOf("pub", "sec"), light = setOf("sec"), card = BoardCard("Suspect one", "A client secret stolen from the Android app?")),
                    Beat(6, light = setOf("pub"), card = BoardCard("Cleared: a public client", "A phone can't keep a secret, so the gym app has none. It uses PKCE.")),
                    Beat(8, show = setOf("pkce", "code"), light = setOf("pkce", "code"), pulse = "pkce" to "code", card = BoardCard("PKCE, how it works", "A scrambled word goes first; the code is swapped only with the original.")),
                ),
            ),
            // 9: "Fine, nothing to steal. Suspect two..."
            Scene(
                9, "Suspect two: permissions",
                DiagramSpec(
                    "Delegated means a signed-in user acts through the app; application means the app acts alone, with admin consent.",
                    nodes = listOf(
                        DNode("del", "Delegated", 0.26f, 0.2f, w = 0.42f, h = 0.2f, kind = NodeKind.PILL, sub = "scp claim"),
                        DNode("appp", "Application", 0.75f, 0.2f, w = 0.42f, h = 0.2f, kind = NodeKind.PILL, sub = "roles claim"),
                        DNode("book", "Booking a class", 0.26f, 0.6f, w = 0.4f, h = 0.18f, tone = 1, sub = "a member is there"),
                        DNode("job", "Nightly report", 0.75f, 0.6f, w = 0.4f, h = 0.18f, tone = 1, sub = "no user"),
                        DNode("admin", "Admin consent, always", 0.75f, 0.9f, w = 0.46f, h = 0.14f, kind = NodeKind.NOTE),
                    ),
                    edges = listOf(
                        DEdge("del", "book"),
                        DEdge("appp", "job"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(9, show = setOf("appp"), light = setOf("appp"), card = BoardCard("Suspect two", "Application permissions on the Android app? Far too much power.")),
                    Beat(10, show = setOf("del", "book"), light = setOf("del", "book"), card = BoardCard("Delegated permission", "A friend with your key: only what you could do. The scp claim.")),
                    Beat(12, show = setOf("job", "admin"), light = setOf("appp", "job", "admin"), pulse = "appp" to "job", card = BoardCard("Application permission", "No user, roles claim, admin consent. Here: the nightly report.")),
                ),
            ),
            // 13: "So no stolen secret, no giant permission. Then the token itself was fake?"
            Scene(
                13, "The clue: the audience",
                DiagramSpec(
                    "The token was real but made for Microsoft Graph; the API checked only the signature, never the audience.",
                    nodes = listOf(
                        DNode("token", "Real token", 0.22f, 0.2f, w = 0.38f, h = 0.18f, kind = NodeKind.PILL, sub = "signed by Microsoft"),
                        DNode("aud", "aud: Microsoft Graph", 0.74f, 0.2f, w = 0.44f, h = 0.18f, kind = NodeKind.NOTE, tone = 2),
                        DNode("api", "Gym API", 0.22f, 0.7f, w = 0.34f, h = 0.2f, sub = "signature only"),
                        DNode("check", "Check the audience", 0.74f, 0.7f, w = 0.44f, h = 0.2f, tone = 1, sub = "only its own id"),
                    ),
                    edges = listOf(
                        DEdge("token", "api", "sent to"),
                        DEdge("api", "check", "the fix"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(13, show = setOf("token", "api"), pulse = "token" to "api"),
                    Beat(14, show = setOf("aud"), light = setOf("aud"), card = BoardCard("The clue", "A real token, made for Graph. Someone sent it to the gym's API.")),
                    Beat(16, show = setOf("check"), light = setOf("api", "check"), pulse = "api" to "check", card = BoardCard("A token for Graph is not for you", "The API must check the audience on every access token.")),
                ),
            ),
            // 17: "Case closed, then?"
            Scene(
                17, "The trainer's button",
                DiagramSpec(
                    "The app reads the ID token for the name and asks the API what the member may do; the access token is sealed, for the API.",
                    nodes = listOf(
                        DNode("phone", "Android app", 0.22f, 0.2f, w = 0.36f, h = 0.2f),
                        DNode("at", "Access token", 0.76f, 0.2f, w = 0.4f, h = 0.18f, kind = NodeKind.PILL, sub = "sealed, for the API"),
                        DNode("idt", "ID token", 0.22f, 0.66f, w = 0.36f, h = 0.18f, kind = NodeKind.PILL, sub = "who: the name"),
                        DNode("api", "Gym API", 0.76f, 0.66f, w = 0.34f, h = 0.2f, sub = "decides"),
                    ),
                    edges = listOf(
                        DEdge("idt", "phone", "shows name"),
                        DEdge("at", "api", "sent to"),
                        DEdge("phone", "api", "may I?"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(18, show = setOf("phone", "at"), light = setOf("at"), card = BoardCard("One more clue", "The app read the access token to show the trainer's cancel button.")),
                    Beat(20, show = setOf("idt", "api"), light = setOf("idt", "api"), pulse = "phone" to "api", card = BoardCard("Phone shows, server decides", "ID token for the name; ask the API what the member may do.")),
                ),
            ),
            // 21: "Let's back up. How does Microsoft even know about the gym app?"
            Scene(
                21, "Registering the app",
                DiagramSpec(
                    "One application object in the home tenant; one service principal per tenant that uses the app, holding its permissions.",
                    nodes = listOf(
                        DNode("obj", "Application object", 0.5f, 0.15f, w = 0.56f, h = 0.2f, tone = 1, sub = "home tenant, client id"),
                        DNode("sp1", "Service principal", 0.24f, 0.58f, w = 0.42f, h = 0.2f, sub = "home tenant"),
                        DNode("sp2", "Service principal", 0.76f, 0.58f, w = 0.42f, h = 0.2f, tone = 2, sub = "another gym chain"),
                        DNode("note", "Roles and consent attach here", 0.5f, 0.9f, w = 0.7f, h = 0.14f, kind = NodeKind.NOTE),
                    ),
                    edges = listOf(
                        DEdge("obj", "sp1", "defines"),
                        DEdge("obj", "sp2", "defines"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(22, show = setOf("obj"), light = setOf("obj"), card = BoardCard("Application object", "The single record of your app: client id, redirect URI, permissions.")),
                    Beat(24, show = setOf("sp1", "sp2", "note"), light = setOf("sp2", "note"), pulse = "obj" to "sp2", card = BoardCard("Service principal", "Each tenant's visitor pass for your app. Permissions attach to it.")),
                ),
            ),
            // 25: "Where does this idea stop working? Like the gym's TV screen..."
            Scene(
                25, "Where it stops",
                DiagramSpec(
                    "Each scenario has its own flow: a user in a browser, no user, API to API, no browser, and the deprecated implicit flow.",
                    nodes = listOf(
                        DNode("code", "Auth code + PKCE", 0.26f, 0.13f, w = 0.46f, h = 0.18f, sub = "a user, a browser"),
                        DNode("cc", "Client credentials", 0.76f, 0.13f, w = 0.44f, h = 0.18f, sub = "no user"),
                        DNode("obo", "On-behalf-of", 0.26f, 0.47f, w = 0.46f, h = 0.18f, tone = 1, sub = "API to API as user"),
                        DNode("dev", "Device code", 0.76f, 0.47f, w = 0.44f, h = 0.18f, tone = 1, sub = "no browser: the TV"),
                        DNode("imp", "Implicit", 0.5f, 0.83f, w = 0.5f, h = 0.18f, tone = 2, sub = "old, deprecated"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(25, show = setOf("dev"), light = setOf("dev")),
                    Beat(26, show = setOf("code", "cc", "obo", "imp"), light = setOf("dev", "obo", "imp"), card = BoardCard("The gym's TV", "No browser: device code. Implicit is deprecated. MSAL: the code.")),
                ),
            ),
            // 27: "So, the case summary for the exam?"
            Scene(
                27, "Case closed",
                DiagramSpec(
                    "The exam's identity questions, each as the phrase to listen for and the answer it points to.",
                    nodes = listOf(
                        DNode("nouser", "Nobody signed in", 0.26f, 0.13f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "application, admin consent"),
                        DNode("own", "User's own data", 0.76f, 0.13f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "delegated"),
                        DNode("spa", "Mobile or SPA", 0.26f, 0.47f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "auth code + PKCE"),
                        DNode("tenant", "Another tenant", 0.76f, 0.47f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "service principal"),
                        DNode("aud", "What must the API check?", 0.5f, 0.83f, w = 0.7f, h = 0.2f, sub = "the audience"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(28, show = setOf("nouser", "own", "spa"), card = BoardCard("Listen for the phrase", "Each exam phrase points straight to one answer. Never a secret on a phone.")),
                    Beat(30, show = setOf("tenant", "aud"), light = setOf("tenant", "aud"), card = BoardCard("What cracked the case", "The API checks the audience of every access token.")),
                    Beat(32, card = BoardCard("Today", "Register an app, note its ids, add User Read, compare platform settings.")),
                ),
            ),
        ),
    )

    private fun identityGossipBoard() = PodcastBoard(
        "az-identity--gossip",
        listOf(
            // 0: "Okay, did you hear what Sam did with the bike rental app?..."
            Scene(
                0, "What Sam did",
                DiagramSpec(
                    "Sam put a client secret inside the Android bike app, and Aisha pulled it out of the app package over lunch.",
                    nodes = listOf(
                        DNode("phone", "Bike app", 0.24f, 0.3f, w = 0.36f, h = 0.2f, sub = "on 20,000 phones"),
                        DNode("secret", "Client secret inside", 0.76f, 0.3f, w = 0.42f, h = 0.2f, kind = NodeKind.NOTE, tone = 2),
                        DNode("pkg", "App package", 0.5f, 0.78f, w = 0.4f, h = 0.18f, tone = 1, sub = "taken apart"),
                    ),
                    edges = listOf(
                        DEdge("phone", "secret", "holds"),
                        DEdge("pkg", "secret", "pulled out"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(0, show = setOf("phone")),
                    Beat(2, show = setOf("secret"), light = setOf("secret"), pulse = "phone" to "secret", card = BoardCard("Sam's secret", "A client secret, inside the Android bike app.")),
                    Beat(4, show = setOf("pkg"), light = setOf("pkg"), pulse = "pkg" to "secret", card = BoardCard("A public client", "Anything on a phone can be taken apart. It can't keep a secret.")),
                ),
            ),
            // 5: "So how does a phone app prove anything without a secret?"
            Scene(
                5, "Aisha's fix: PKCE",
                DiagramSpec(
                    "Public clients can't keep a secret, so they use PKCE; confidential clients on a server hold a secret or certificate.",
                    nodes = listOf(
                        DNode("pub", "Public client", 0.26f, 0.2f, w = 0.44f, h = 0.22f, sub = "mobile, desktop, SPA"),
                        DNode("conf", "Confidential client", 0.75f, 0.2f, w = 0.44f, h = 0.22f, tone = 1, sub = "web app, server job"),
                        DNode("pkce", "PKCE", 0.26f, 0.65f, w = 0.34f, kind = NodeKind.PILL, sub = "ticket + secret word"),
                        DNode("sec", "Secret or certificate", 0.75f, 0.65f, w = 0.44f, kind = NodeKind.PILL, tone = 1),
                    ),
                    edges = listOf(
                        DEdge("pub", "pkce"),
                        DEdge("conf", "sec"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(6, show = setOf("pub", "pkce"), light = setOf("pkce"), pulse = "pub" to "pkce", card = BoardCard("PKCE: a cloakroom", "A new secret word each sign-in; a copied code alone is useless.")),
                    Beat(7, show = setOf("conf", "sec"), light = setOf("conf", "sec"), card = BoardCard("Confidential client", "A web app or a job on a server: it holds the secret or certificate.")),
                ),
            ),
            // 8: "Ben. Ben built the premium features..."
            Scene(
                8, "Ben's e-bike button",
                DiagramSpec(
                    "The ID token is for the app; the access token goes to the API, which decides; the refresh token gets new ones.",
                    nodes = listOf(
                        DNode("idt", "ID token", 0.2f, 0.15f, w = 0.34f, h = 0.18f, kind = NodeKind.PILL, sub = "who the rider is"),
                        DNode("at", "Access token", 0.6f, 0.15f, w = 0.34f, h = 0.18f, kind = NodeKind.PILL, sub = "what it may do"),
                        DNode("rt", "Refresh", 0.88f, 0.5f, w = 0.22f, h = 0.18f, kind = NodeKind.PILL, tone = 2, sub = "new tokens"),
                        DNode("app", "Bike app", 0.2f, 0.8f, w = 0.32f, h = 0.2f),
                        DNode("api", "Bike API", 0.6f, 0.8f, w = 0.32f, h = 0.2f, sub = "decides"),
                    ),
                    edges = listOf(
                        DEdge("idt", "app", "show name"),
                        DEdge("at", "api", "sent to"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(8, show = setOf("app", "at"), light = setOf("at"), card = BoardCard("Ben's button", "The app read the access token to decide on the e-bike button.")),
                    Beat(10, show = setOf("idt", "api", "rt"), light = setOf("idt", "at"), pulse = "at" to "api", card = BoardCard("Under the picture", "A JWT of signed claims. ID token: for the app. Access token: for the API.")),
                    Beat(12, light = setOf("api", "rt"), card = BoardCard("The API decides", "Read the ID token for the name; ask the API if the rider is premium.")),
                ),
            ),
            // 13: "Who's next? I heard the night job had a meltdown."
            Scene(
                13, "Nadia's mix-up",
                DiagramSpec(
                    "Riders working with their own rentals need delegated permissions; the nightly job with no user needs an application permission.",
                    nodes = listOf(
                        DNode("del", "Delegated", 0.26f, 0.2f, w = 0.42f, h = 0.2f, kind = NodeKind.PILL, sub = "a user is there"),
                        DNode("appp", "Application", 0.75f, 0.2f, w = 0.42f, h = 0.2f, kind = NodeKind.PILL, sub = "no user at all"),
                        DNode("riders", "Riders' rentals", 0.26f, 0.6f, w = 0.4f, h = 0.18f, tone = 1, sub = "scp claim"),
                        DNode("job", "Battery job", 0.75f, 0.6f, w = 0.4f, h = 0.18f, tone = 1, sub = "roles claim"),
                        DNode("admin", "Admin consent, always", 0.75f, 0.9f, w = 0.46f, h = 0.14f, kind = NodeKind.NOTE),
                    ),
                    edges = listOf(
                        DEdge("del", "riders"),
                        DEdge("appp", "job"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(14, show = setOf("appp"), light = setOf("appp"), card = BoardCard("Application, for riders?", "They sounded stronger. Riders got an admin approval screen.")),
                    Beat(16, show = setOf("del", "admin"), light = setOf("del", "admin"), card = BoardCard("Delegated permission", "On behalf of the signed-in rider. Arrives as the scp claim.")),
                    Beat(18, show = setOf("riders", "job"), light = setOf("riders", "job"), pulse = "appp" to "job", card = BoardCard("Is a person there?", "Riders: delegated. Nightly job: application, client credentials.")),
                ),
            ),
            // 19: "Any more? This office is a disaster."
            Scene(
                19, "Ravi's audience",
                DiagramSpec(
                    "A real token made for Microsoft Graph unlocked a bike, because the API checked only the signature, never the audience.",
                    nodes = listOf(
                        DNode("token", "Graph token", 0.22f, 0.2f, w = 0.38f, h = 0.18f, kind = NodeKind.PILL, sub = "signed by Microsoft"),
                        DNode("aud", "aud: Microsoft Graph", 0.74f, 0.2f, w = 0.44f, h = 0.18f, kind = NodeKind.NOTE, tone = 2),
                        DNode("api", "Bike API", 0.22f, 0.7f, w = 0.34f, h = 0.2f, sub = "signature only"),
                        DNode("check", "Check the audience", 0.74f, 0.7f, w = 0.44f, h = 0.2f, tone = 1, sub = "only its own id"),
                    ),
                    edges = listOf(
                        DEdge("token", "api", "sent to"),
                        DEdge("api", "check", "the fix"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(20, show = setOf("api"), light = setOf("api"), card = BoardCard("Ravi's API", "Checked the signature, and stopped there.")),
                    Beat(22, show = setOf("token", "aud", "check"), light = setOf("aud", "check"), pulse = "token" to "api", card = BoardCard("A token for Graph is not for you", "It unlocked a bike. Now the API accepts only its own tokens.")),
                ),
            ),
            // 23: "How does the bike API get an audience of its own, anyway?"
            Scene(
                23, "Registering the app",
                DiagramSpec(
                    "One application object in the home tenant; one service principal per tenant that uses the app, holding its permissions.",
                    nodes = listOf(
                        DNode("obj", "Application object", 0.5f, 0.15f, w = 0.56f, h = 0.2f, tone = 1, sub = "home tenant, client id"),
                        DNode("sp1", "Service principal", 0.24f, 0.58f, w = 0.42f, h = 0.2f, sub = "home tenant"),
                        DNode("sp2", "Service principal", 0.76f, 0.58f, w = 0.42f, h = 0.2f, tone = 2, sub = "the city council"),
                        DNode("note", "Roles and consent attach here", 0.5f, 0.9f, w = 0.7f, h = 0.14f, kind = NodeKind.NOTE),
                    ),
                    edges = listOf(
                        DEdge("obj", "sp1", "defines"),
                        DEdge("obj", "sp2", "defines"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(24, show = setOf("obj"), light = setOf("obj"), card = BoardCard("Application object", "Client id, redirect URI, permissions. The API's id becomes the audience.")),
                    Beat(26, show = setOf("sp1", "sp2", "note"), light = setOf("sp2", "note"), pulse = "obj" to "sp2", card = BoardCard("Service principal", "The council's visitor pass for your app. Permissions attach here.")),
                ),
            ),
            // 27: "Where does all this stop working? Like the docking station screens?"
            Scene(
                27, "Where it stops",
                DiagramSpec(
                    "Each scenario has its own flow: a user in a browser, no user, API to API, no browser, and the deprecated implicit flow.",
                    nodes = listOf(
                        DNode("code", "Auth code + PKCE", 0.26f, 0.13f, w = 0.46f, h = 0.18f, sub = "a user, a browser"),
                        DNode("cc", "Client credentials", 0.76f, 0.13f, w = 0.44f, h = 0.18f, sub = "no user"),
                        DNode("obo", "On-behalf-of", 0.26f, 0.47f, w = 0.46f, h = 0.18f, tone = 1, sub = "API to API as user"),
                        DNode("dev", "Device code", 0.76f, 0.47f, w = 0.44f, h = 0.18f, tone = 1, sub = "no browser"),
                        DNode("imp", "Implicit", 0.5f, 0.83f, w = 0.5f, h = 0.18f, tone = 2, sub = "old, deprecated"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(27, show = setOf("dev"), light = setOf("dev"), card = BoardCard("Docking station screens", "No browser: device code, typed on your phone.")),
                    Beat(28, show = setOf("code", "cc", "obo", "imp"), light = setOf("obo", "imp"), card = BoardCard("On-behalf-of; implicit is old", "API calls API as the rider. Pick auth code with PKCE. MSAL: the code.")),
                ),
            ),
            // 29: "So, the gossip I should remember for the exam?"
            Scene(
                29, "For the exam",
                DiagramSpec(
                    "The exam's identity questions, each as the phrase to listen for and the answer it points to.",
                    nodes = listOf(
                        DNode("nouser", "Nobody signed in", 0.26f, 0.13f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "application, admin consent"),
                        DNode("own", "User's own data", 0.76f, 0.13f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "delegated"),
                        DNode("spa", "Mobile or SPA", 0.26f, 0.47f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "auth code + PKCE"),
                        DNode("tenant", "Another tenant", 0.76f, 0.47f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "service principal"),
                        DNode("aud", "What must the API check?", 0.5f, 0.83f, w = 0.7f, h = 0.2f, sub = "the audience"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(30, show = setOf("nouser", "own", "spa", "tenant", "aud"), light = setOf("spa", "aud"), card = BoardCard("Listen for the phrase", "Each exam phrase points straight to one answer. Never a secret on a phone.")),
                    Beat(32, card = BoardCard("Today", "Register an app, note its ids, add User Read, compare platform settings.")),
                ),
            ),
        ),
    )

    private fun msalCaseBoard() = PodcastBoard(
        "az-msal--case-files",
        listOf(
            // 0: "Welcome to Case Files. Today's case: The Endless Sign-in..."
            Scene(
                0, "The Endless Sign-in",
                DiagramSpec(
                    "Since the update, the plant watering app shows the Microsoft sign-in window on every single launch.",
                    nodes = listOf(
                        DNode("app", "Plant app", 0.2f, 0.35f, w = 0.32f, h = 0.22f, sub = "Android"),
                        DNode("idp", "Microsoft sign-in", 0.72f, 0.35f, w = 0.5f, h = 0.32f, kind = NodeKind.CLOUD),
                        DNode("win", "Sign-in, every launch", 0.5f, 0.82f, w = 0.56f, h = 0.2f, kind = NodeKind.NOTE, tone = 2),
                    ),
                    edges = listOf(
                        DEdge("app", "idp", "every time"),
                        DEdge("idp", "win", dashed = true),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(0, show = setOf("app", "idp", "win"), light = setOf("win"), pulse = "app" to "idp", card = BoardCard("The case", "Every launch shows the sign-in window. The reviews are brutal.")),
                ),
            ),
            // 2: "With the tool. The app gets its tokens through MSAL..."
            Scene(
                2, "What MSAL does",
                DiagramSpec(
                    "MSAL talks to the sign-in service for you: it shows the window, caches tokens on the device and renews them.",
                    nodes = listOf(
                        DNode("app", "Plant app", 0.18f, 0.2f, w = 0.3f, h = 0.2f),
                        DNode("msal", "MSAL", 0.5f, 0.55f, w = 0.34f, h = 0.2f, sub = "a library in the app"),
                        DNode("idp", "Microsoft sign-in", 0.72f, 0.2f, w = 0.5f, h = 0.32f, kind = NodeKind.CLOUD),
                        DNode("cache", "Token cache", 0.5f, 0.88f, w = 0.34f, h = 0.22f, kind = NodeKind.CYLINDER),
                    ),
                    edges = listOf(
                        DEdge("app", "msal", "scopes"),
                        DEdge("msal", "idp"),
                        DEdge("msal", "cache", bidirectional = true),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(2, show = setOf("app", "msal", "idp", "cache"), light = setOf("msal"), pulse = "app" to "msal", card = BoardCard("MSAL", "Microsoft Authentication Library: sign-in window, token cache, renewals.")),
                    Beat(4, light = setOf("msal", "cache"), pulse = "msal" to "idp", card = BoardCard("Under the picture", "A library in your app. On Android: a browser sign-in and a redirect.")),
                ),
            ),
            // 5: "Then my first suspect: the tokens expire too fast..."
            Scene(
                5, "The suspects",
                DiagramSpec(
                    "Short tokens and a broken cache are cleared; the update calls the interactive method on every launch.",
                    nodes = listOf(
                        DNode("exp", "Tokens too short", 0.26f, 0.2f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "an hour is fine"),
                        DNode("cache", "A broken cache", 0.76f, 0.2f, w = 0.42f, h = 0.2f, kind = NodeKind.NOTE, sub = "never even asked"),
                        DNode("code", "Interactive every launch", 0.5f, 0.74f, w = 0.66f, h = 0.22f, sub = "the real cause"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(5, show = setOf("exp"), light = setOf("exp")),
                    Beat(6, card = BoardCard("Suspect one: cleared", "An access token lasts about an hour; the refresh token renews it.")),
                    Beat(7, show = setOf("cache"), light = setOf("cache")),
                    Beat(8, show = setOf("code"), light = setOf("code"), card = BoardCard("The clue", "On every launch the update calls AcquireTokenInteractive. No cache.")),
                ),
            ),
            // 10: "Exactly. The right pattern is silent first..."
            Scene(
                10, "Silent first",
                DiagramSpec(
                    "Always try silent first; fall back to an interactive prompt only on MsalUiRequiredException.",
                    nodes = listOf(
                        DNode("silent", "AcquireToken Silent", 0.22f, 0.3f, w = 0.4f, h = 0.22f, sub = "cache or refresh"),
                        DNode("tok", "Token", 0.75f, 0.13f, w = 0.3f, kind = NodeKind.PILL, tone = 2),
                        DNode("ex", "MsalUiRequired Exception", 0.75f, 0.47f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE),
                        DNode("inter", "AcquireToken Interactive", 0.5f, 0.84f, w = 0.48f, h = 0.22f, sub = "sign-in window"),
                    ),
                    edges = listOf(
                        DEdge("silent", "tok", "hit"),
                        DEdge("silent", "ex", "can't"),
                        DEdge("ex", "inter"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(10, show = setOf("silent", "tok"), light = setOf("silent"), pulse = "silent" to "tok", card = BoardCard("AcquireTokenSilent", "Cache first; an expired token is renewed with the refresh token.")),
                    Beat(12, show = setOf("ex"), light = setOf("ex"), pulse = "silent" to "ex", card = BoardCard("MsalUiRequiredException", "The user must be asked: consent, a second check, or a lapsed session.")),
                    Beat(14, show = setOf("inter"), light = setOf("inter"), pulse = "ex" to "inter", card = BoardCard("C#", "try { AcquireTokenSilent } catch (MsalUiRequiredException) { AcquireTokenInteractive }", code = true)),
                    Beat(16, light = setOf("silent", "tok"), card = BoardCard("The first run", "Silent throws on an empty cache. Sign in once, then months of silent.")),
                ),
            ),
            // 17: "Mostly? There's more, isn't there."
            Scene(
                17, "The second witness",
                DiagramSpec(
                    "The public client builder is for apps on a device; the confidential client builder, and any secret, is for servers.",
                    nodes = listOf(
                        DNode("pub", "Public client", 0.26f, 0.2f, w = 0.44f, h = 0.22f, sub = "mobile, desktop, CLI"),
                        DNode("conf", "Confidential client", 0.75f, 0.2f, w = 0.44f, h = 0.22f, tone = 1, sub = "web app, API, job"),
                        DNode("phone", "Plant app", 0.26f, 0.62f, w = 0.42f, h = 0.18f, tone = 1, sub = "no secret"),
                        DNode("server", "Nightly report job", 0.75f, 0.62f, w = 0.42f, h = 0.18f, tone = 1, sub = "secret in Key Vault"),
                        DNode("def", "/.default", 0.75f, 0.9f, w = 0.34f, h = 0.14f, kind = NodeKind.PILL, sub = "all consented"),
                    ),
                    edges = listOf(
                        DEdge("pub", "phone"),
                        DEdge("conf", "server"),
                        DEdge("server", "def", "scope"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(18, show = setOf("phone"), light = setOf("phone"), card = BoardCard("The theory", "Make the app confidential, with a secret inside? Never.")),
                    Beat(20, show = setOf("pub", "conf"), light = setOf("pub", "conf"), pulse = "pub" to "phone", card = BoardCard("Two builders", "Public: apps on a device. Confidential: web apps, APIs and jobs.")),
                    Beat(22, show = setOf("server"), light = setOf("server"), pulse = "conf" to "server", card = BoardCard("C#", "AcquireTokenForClient(new[] { \"api://plants/.default\" })", code = true)),
                    Beat(24, show = setOf("def"), light = setOf("def"), pulse = "server" to "def", card = BoardCard("/.default", "Every permission already consented on that resource. No user to ask.")),
                ),
            ),
            // 25: "And the second witness?"
            Scene(
                25, "One token, one audience",
                DiagramSpec(
                    "Each access token has a single audience, so ask for Graph scopes and your API's scopes in separate calls.",
                    nodes = listOf(
                        DNode("app", "Plant app", 0.18f, 0.45f, w = 0.3f, h = 0.2f),
                        DNode("t1", "Token 1", 0.5f, 0.2f, w = 0.24f, h = 0.14f, kind = NodeKind.PILL, tone = 1),
                        DNode("t2", "Token 2", 0.5f, 0.7f, w = 0.24f, h = 0.14f, kind = NodeKind.PILL, tone = 1),
                        DNode("graph", "Microsoft Graph", 0.83f, 0.2f, w = 0.3f, h = 0.2f),
                        DNode("api", "Plant API", 0.83f, 0.7f, w = 0.3f, h = 0.2f),
                        DNode("oa", "offline access: refresh", 0.5f, 0.93f, w = 0.6f, h = 0.12f, kind = NodeKind.NOTE, tone = 2),
                    ),
                    edges = listOf(
                        DEdge("app", "t1"),
                        DEdge("app", "t2"),
                        DEdge("t1", "graph", "audience"),
                        DEdge("t2", "api", "audience"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(26, show = setOf("app", "graph", "api"), light = setOf("app"), card = BoardCard("The second witness", "Graph's User Read and the plant API's scope in one call. It failed.")),
                    Beat(28, show = setOf("t1", "t2"), light = setOf("t1", "t2"), pulse = "t1" to "graph", card = BoardCard("A train ticket for one line", "One audience per token: Graph and your API in two separate calls.")),
                    Beat(30, show = setOf("oa"), light = setOf("oa"), card = BoardCard("Sign-in scopes", "openid and profile come from OpenID Connect. offline_access: a refresh token.")),
                ),
            ),
            // 31: "Where does all this stop working? Like the plant shop's web dashboard?"
            Scene(
                31, "Where it stops",
                DiagramSpec(
                    "The other MSAL calls, each for its own scenario, and a shared token cache for web apps on many servers.",
                    nodes = listOf(
                        DNode("obo", "OnBehalfOf", 0.26f, 0.13f, w = 0.46f, h = 0.18f, sub = "API calls API as user"),
                        DNode("dev", "WithDeviceCode", 0.76f, 0.13f, w = 0.44f, h = 0.18f, sub = "no browser"),
                        DNode("code", "ByAuthorizationCode", 0.5f, 0.45f, w = 0.56f, h = 0.18f, sub = "web app signs users in"),
                        DNode("web", "Identity Web", 0.22f, 0.82f, w = 0.34f, h = 0.2f, tone = 1),
                        DNode("redis", "Shared cache", 0.76f, 0.82f, w = 0.34f, h = 0.24f, kind = NodeKind.CYLINDER, sub = "Redis or SQL"),
                    ),
                    edges = listOf(
                        DEdge("web", "redis", "tokens"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(32, show = setOf("web", "redis"), light = setOf("redis"), pulse = "web" to "redis", card = BoardCard("Three servers", "Tokens in each server's memory vanish. Use a shared cache: Redis or SQL.")),
                    Beat(34, show = setOf("obo", "dev", "code"), light = setOf("obo", "dev", "code"), card = BoardCard("AcquireToken...", "...WithDeviceCode, OnBehalfOf, ByAuthorizationCode")),
                ),
            ),
            // 35: "So, the case summary for the exam?"
            Scene(
                35, "Case closed",
                DiagramSpec(
                    "The exam's MSAL questions, each as the phrase to listen for and the answer it points to.",
                    nodes = listOf(
                        DNode("prompts", "Constant prompts", 0.26f, 0.13f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "silent first"),
                        DNode("daemon", "Daemon, no user", 0.76f, 0.13f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "ForClient, /.default"),
                        DNode("mix", "Two APIs, one call", 0.26f, 0.47f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "one audience"),
                        DNode("nobrowser", "No browser", 0.76f, 0.47f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "device code"),
                        DNode("builders", "Users or servers?", 0.5f, 0.83f, w = 0.7f, h = 0.2f, sub = "public or confidential builder"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(36, show = setOf("prompts", "daemon", "mix", "nobrowser", "builders"), light = setOf("prompts"), card = BoardCard("Listen for the phrase", "Each exam phrase points straight to one answer. Silent first cracked it.")),
                    Beat(38, card = BoardCard("Today", "Console app, a token interactively, decode aud and scp, then the silent path.")),
                ),
            ),
        ),
    )

    private fun msalGossipBoard() = PodcastBoard(
        "az-msal--gossip",
        listOf(
            // 0: "Okay, did you hear what Ben did to the quiz app?..."
            Scene(
                0, "What Ben did",
                DiagramSpec(
                    "Ben's quiz app called the interactive sign-in before every request, so every answer brought up the sign-in window.",
                    nodes = listOf(
                        DNode("app", "Quiz app", 0.2f, 0.35f, w = 0.32f, h = 0.22f, sub = "Android"),
                        DNode("idp", "Microsoft sign-in", 0.72f, 0.35f, w = 0.5f, h = 0.32f, kind = NodeKind.CLOUD),
                        DNode("win", "Sign-in, every answer", 0.5f, 0.82f, w = 0.56f, h = 0.2f, kind = NodeKind.NOTE, tone = 2),
                    ),
                    edges = listOf(
                        DEdge("app", "idp", "every request"),
                        DEdge("idp", "win", dashed = true),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(0, show = setOf("app", "idp")),
                    Beat(2, light = setOf("app", "idp"), pulse = "app" to "idp", card = BoardCard("MSAL", "Microsoft Authentication Library: sign-in window, token cache, renewals.")),
                    Beat(4, show = setOf("win"), light = setOf("win"), card = BoardCard("Ben's sign-in", "AcquireTokenInteractive before every request. Three questions, tops.")),
                ),
            ),
            // 5: "I'd last two. Who fixed it?"
            Scene(
                5, "Priya: silent first",
                DiagramSpec(
                    "Always try silent first; fall back to an interactive prompt only on MsalUiRequiredException.",
                    nodes = listOf(
                        DNode("silent", "AcquireToken Silent", 0.22f, 0.3f, w = 0.4f, h = 0.22f, sub = "cache or refresh"),
                        DNode("tok", "Token", 0.75f, 0.13f, w = 0.3f, kind = NodeKind.PILL, tone = 2),
                        DNode("ex", "MsalUiRequired Exception", 0.75f, 0.47f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE),
                        DNode("inter", "AcquireToken Interactive", 0.5f, 0.84f, w = 0.48f, h = 0.22f, sub = "sign-in window"),
                    ),
                    edges = listOf(
                        DEdge("silent", "tok", "hit"),
                        DEdge("silent", "ex", "can't"),
                        DEdge("ex", "inter"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(6, show = setOf("silent", "tok"), light = setOf("silent"), pulse = "silent" to "tok", card = BoardCard("AcquireTokenSilent", "A loyalty card: cache first, renewed with the refresh token.")),
                    Beat(8, show = setOf("ex", "inter"), light = setOf("ex", "inter"), pulse = "ex" to "inter", card = BoardCard("MsalUiRequiredException", "The player must be asked. Only then interactive. Retrying won't help.")),
                    Beat(10, light = setOf("silent", "tok"), card = BoardCard("For months", "First run: sign in once. After that, the refresh token keeps it silent.")),
                    Beat(12, card = BoardCard("Under the picture", "A library in your app: runs the sign-in flows, caches tokens on the device.")),
                ),
            ),
            // 13: "Okay, who's next?"
            Scene(
                13, "The intern's secret",
                DiagramSpec(
                    "The public client builder is for apps on a device and holds no secret; the confidential builder is for servers.",
                    nodes = listOf(
                        DNode("pub", "Public client", 0.26f, 0.2f, w = 0.44f, h = 0.22f, sub = "mobile, desktop, CLI"),
                        DNode("conf", "Confidential client", 0.75f, 0.2f, w = 0.44f, h = 0.22f, tone = 1, sub = "web app, API, job"),
                        DNode("phone", "Quiz app", 0.26f, 0.62f, w = 0.42f, h = 0.18f, tone = 1, sub = "no secret"),
                        DNode("server", "Quiz API", 0.75f, 0.62f, w = 0.42f, h = 0.18f, tone = 1, sub = "secrets in Key Vault"),
                    ),
                    edges = listOf(
                        DEdge("pub", "phone"),
                        DEdge("conf", "server"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(14, show = setOf("conf", "phone"), light = setOf("conf", "phone"), card = BoardCard("The intern", "The confidential builder, and a client secret inside the Android app.")),
                    Beat(16, show = setOf("pub", "server"), light = setOf("pub", "phone"), pulse = "pub" to "phone", card = BoardCard("Two builders", "Public: apps on a device. Confidential: web apps, APIs and jobs.")),
                ),
            ),
            // 17: "Who's next?"
            Scene(
                17, "Aisha's two scopes",
                DiagramSpec(
                    "Each access token has a single audience, so ask for Graph scopes and your API's scopes in separate calls.",
                    nodes = listOf(
                        DNode("app", "Quiz app", 0.18f, 0.45f, w = 0.3f, h = 0.2f),
                        DNode("t1", "Token 1", 0.5f, 0.2f, w = 0.24f, h = 0.14f, kind = NodeKind.PILL, tone = 1),
                        DNode("t2", "Token 2", 0.5f, 0.7f, w = 0.24f, h = 0.14f, kind = NodeKind.PILL, tone = 1),
                        DNode("graph", "Microsoft Graph", 0.83f, 0.2f, w = 0.3f, h = 0.2f),
                        DNode("api", "Quiz API", 0.83f, 0.7f, w = 0.3f, h = 0.2f),
                        DNode("oa", "offline access: refresh", 0.5f, 0.93f, w = 0.6f, h = 0.12f, kind = NodeKind.NOTE, tone = 2),
                    ),
                    edges = listOf(
                        DEdge("app", "t1"),
                        DEdge("app", "t2"),
                        DEdge("t1", "graph", "audience"),
                        DEdge("t2", "api", "audience"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(18, show = setOf("app", "graph", "api"), light = setOf("app"), card = BoardCard("Aisha's leaderboard", "Graph's User Read and the quiz API's scope in one call.")),
                    Beat(20, show = setOf("t1", "t2"), light = setOf("t1", "t2"), pulse = "t1" to "graph", card = BoardCard("A train ticket for one line", "One audience per token: Graph and your API in two separate calls.")),
                    Beat(22, show = setOf("oa"), light = setOf("oa"), card = BoardCard("Sign-in scopes", "openid and profile come from OpenID Connect. offline_access: a refresh token.")),
                ),
            ),
            // 23: "Noted. And I heard about a leaderboard job?"
            Scene(
                23, "Felix's Sunday job",
                DiagramSpec(
                    "A job with no user uses client credentials: AcquireTokenForClient with the resource's /.default scope.",
                    nodes = listOf(
                        DNode("job", "Weekly winners job", 0.24f, 0.2f, w = 0.4f, h = 0.2f, sub = "confidential client"),
                        DNode("for", "AcquireToken ForClient", 0.74f, 0.2f, w = 0.46f, h = 0.2f),
                        DNode("def", "/.default", 0.74f, 0.6f, w = 0.34f, kind = NodeKind.PILL, sub = "everything consented"),
                        DNode("nope", "Silent, User Read: fail", 0.24f, 0.6f, w = 0.4f, kind = NodeKind.NOTE, tone = 2),
                    ),
                    edges = listOf(
                        DEdge("job", "for"),
                        DEdge("for", "def", "scope"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(24, show = setOf("job", "nope"), light = setOf("nope"), card = BoardCard("Felix's first try", "Silent, then User Read. With no player signed in, neither works.")),
                    Beat(26, show = setOf("for", "def"), light = setOf("for", "def"), pulse = "job" to "for", card = BoardCard("C#", "AcquireTokenForClient(new[] { \"api://quiz/.default\" })", code = true)),
                ),
            ),
            // 27: "And the last one? There's always a last one."
            Scene(
                27, "Tess's three servers",
                DiagramSpec(
                    "Tokens kept in each server's memory vanish when the next request lands elsewhere; a shared cache serves them all.",
                    nodes = listOf(
                        DNode("s1", "Server 1", 0.18f, 0.2f, w = 0.26f, h = 0.18f, sub = "own memory"),
                        DNode("s2", "Server 2", 0.5f, 0.2f, w = 0.26f, h = 0.18f, sub = "own memory"),
                        DNode("s3", "Server 3", 0.82f, 0.2f, w = 0.26f, h = 0.18f, sub = "own memory"),
                        DNode("redis", "Shared cache", 0.5f, 0.75f, w = 0.36f, h = 0.26f, kind = NodeKind.CYLINDER, sub = "Redis or SQL"),
                    ),
                    edges = listOf(
                        DEdge("s1", "redis"),
                        DEdge("s2", "redis"),
                        DEdge("s3", "redis"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(28, show = setOf("s1", "s2", "s3"), light = setOf("s1", "s2", "s3"), card = BoardCard("Signed out at random", "Tokens in each server's memory vanish when the next request moves.")),
                    Beat(30, show = setOf("redis"), light = setOf("redis"), pulse = "s2" to "redis", card = BoardCard("Microsoft Identity Web", "Wraps MSAL for dot NET web apps; tokens go in a shared cache.")),
                ),
            ),
            // 31: "Where does all this stop working? Like the quiz night screen at the pub?"
            Scene(
                31, "Where it stops",
                DiagramSpec(
                    "No browser means device code; an API calling another API as the user means on-behalf-of.",
                    nodes = listOf(
                        DNode("dev", "WithDeviceCode", 0.26f, 0.3f, w = 0.46f, h = 0.2f, sub = "no browser"),
                        DNode("obo", "OnBehalfOf", 0.76f, 0.3f, w = 0.44f, h = 0.2f, sub = "API calls API as user"),
                        DNode("phone", "Code typed on a phone", 0.26f, 0.75f, w = 0.46f, h = 0.18f, kind = NodeKind.NOTE),
                    ),
                    edges = listOf(
                        DEdge("dev", "phone"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(32, show = setOf("dev", "obo", "phone"), light = setOf("dev", "phone"), pulse = "dev" to "phone", card = BoardCard("Quiz night screen", "No browser: device code, typed on your phone.")),
                ),
            ),
            // 33: "So, the gossip I should remember for the exam?"
            Scene(
                33, "For the exam",
                DiagramSpec(
                    "The exam's MSAL questions, each as the phrase to listen for and the answer it points to.",
                    nodes = listOf(
                        DNode("prompts", "Constant prompts", 0.26f, 0.13f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "silent first"),
                        DNode("daemon", "Daemon, no user", 0.76f, 0.13f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "ForClient, /.default"),
                        DNode("mix", "Two APIs, one call", 0.26f, 0.47f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "one audience"),
                        DNode("nobrowser", "No browser", 0.76f, 0.47f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "device code"),
                        DNode("builders", "Users or servers?", 0.5f, 0.83f, w = 0.7f, h = 0.2f, sub = "public or confidential builder"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(34, show = setOf("prompts", "daemon", "mix", "nobrowser", "builders"), light = setOf("prompts"), card = BoardCard("Listen for the phrase", "Each exam phrase points straight to one answer.")),
                    Beat(36, card = BoardCard("Today", "Console app, a token interactively, decode aud and scp, then the silent path.")),
                ),
            ),
        ),
    )

    val boards: List<PodcastBoard> by lazy { listOf(identityCaseBoard(), identityGossipBoard(), msalCaseBoard(), msalGossipBoard()) }
}
