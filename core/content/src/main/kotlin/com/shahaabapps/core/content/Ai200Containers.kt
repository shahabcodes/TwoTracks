package com.shahaabapps.core.content

/** AI-200 topics, written from the official study guide and Microsoft Learn (Ai200). */
internal object Ai200Containers {
    private const val LEARN = "https://learn.microsoft.com/en-us/"

    private val acr = Topic(
        id = "ai-acr", title = "Azure Container Registry: build, store and version images", minutes = 26,
        references = listOf(
            B.ref("Introduction to Azure Container Registry", LEARN + "azure/container-registry/container-registry-intro"),
            B.ref("Azure Container Registry service tiers", LEARN + "azure/container-registry/container-registry-skus"),
            B.ref("Automate container builds with ACR Tasks", LEARN + "azure/container-registry/container-registry-tasks-overview"),
            B.ref("Run multi-step build, test and patch tasks", LEARN + "azure/container-registry/container-registry-tasks-multi-step"),
            B.ref("Registry authentication options", LEARN + "azure/container-registry/container-registry-authentication"),
            B.ref("Geo-replication in Azure Container Registry", LEARN + "azure/container-registry/container-registry-geo-replication"),
            B.ref("Learn module: Store and manage containers in Azure Container Registry", LEARN + "training/modules/store-manage-containers-azure-container-registry/"),
        ),
        blocks = listOf(
            B.warmup(
                "ai-acr-w1", "ai-acr",
                "You push coach-api:latest twice in one day. Which name always points at exactly the first image you pushed?",
                listOf("Its digest, the sha256 value", "The tag latest", "The repository name coach-api"), 0,
                "One of these names can be moved to another image at any time. One can never move.",
            ),
            B.plain("""
                A container image is a packed-up copy of your app and everything it needs to run. Azure Container
                Registry (ACR) is a private, managed place in Azure to keep those images, like a private Docker Hub.
                It can also build the images for you in the cloud, so you do not need Docker on your laptop.
            """),
            B.diagram(
                caption = "ACR Tasks builds the image from source and pushes it to the registry. Hosts pull it with an identity that holds AcrPull.",
                nodes = listOf(
                    DNode("src", "Source code", 0.15f, 0.2f, w = 0.26f, h = 0.18f, sub = "Dockerfile + app"),
                    DNode("task", "ACR Tasks build", 0.5f, 0.2f, w = 0.3f, h = 0.18f, tone = 1),
                    DNode("acr", "Registry", 0.5f, 0.55f, w = 0.34f, h = 0.24f, kind = NodeKind.CYLINDER, sub = "coach-api:1.4.0"),
                    DNode("role", "AcrPull role", 0.85f, 0.55f, w = 0.26f, h = 0.14f, kind = NodeKind.NOTE, tone = 2),
                    DNode("app", "App Service", 0.2f, 0.88f, w = 0.3f, h = 0.16f, tone = 1),
                    DNode("aca", "Container Apps", 0.8f, 0.88f, w = 0.3f, h = 0.16f, tone = 1),
                ),
                edges = listOf(
                    DEdge("src", "task", label = "az acr build"), DEdge("task", "acr", label = "push"),
                    DEdge("acr", "app", label = "pull"), DEdge("acr", "aca", label = "pull"), DEdge("role", "acr", dashed = true),
                ),
                aspect = 0.8f,
            ),
            B.h("Registries, repositories, tags and digests"),
            B.p("""
                A registry has a login server name such as ttcoach.azurecr.io. Inside it, a repository is a folder of
                versions of one image, for example coach-api. Each version is found by a tag (a friendly, movable label
                like 1.4.0 or latest) and by a digest (a sha256 hash of the image's content that never changes).
            """),
            B.table(
                listOf("Name", "Example", "Can it move?"),
                listOf("Repository", "coach-api", "It is just the folder"),
                listOf("Tag", "coach-api:1.4.0", "Yes; a new push can reuse it"),
                listOf("Digest", "coach-api@sha256:4f9a...", "Never; it is the content's fingerprint"),
            ),
            B.bullets(
                "Tag each build with something unique (a version or the build id) and deploy that; latest is fine for a laptop, risky in production.",
                "To make one version unchangeable, lock it: az acr repository update with --write-enabled false.",
                "Old untagged images still cost storage; delete them or use a retention policy.",
            ),
            B.h("Tiers"),
            B.compare("Which tier?",
                B.card("Basic", "Learning and small projects.", "Lowest price and storage", "Same API as the others", "No geo-replication or private link"),
                B.card("Standard", "Most production apps.", "More storage and throughput", "Still a single region"),
                B.card("Premium", "Many regions or private networking.", "Geo-replication to other regions", "Private endpoints", "Highest throughput"),
            ),
            B.tip("\"Pull from a registry close to users in two regions\" means geo-replication, and geo-replication means Premium. One registry name, copies in each region, and pulls go to the nearest one."),
            B.check(
                "ai-acr-k1", "ai-acr",
                "Two Tracks Coach runs in West Europe and East US and both must pull images locally from one registry name. What do you need?",
                listOf("A Premium registry with geo-replication", "Two Basic registries with the same name", "A Standard registry and a CDN"), 0,
                "Geo-replication keeps one login server and replicas in each region, and it is a Premium feature.",
                whyWrong = mapOf(
                    1 to "Registry names are unique across Azure, and two registries are two names to manage.",
                    2 to "Standard has no geo-replication, and a CDN does not serve container pulls.",
                ),
            ),
            B.h("ACR Tasks: build in the cloud"),
            B.table(
                listOf("Kind", "How it starts", "Command"),
                listOf("Quick task", "You run it once, like docker build and push in Azure", "az acr build"),
                listOf("Triggered task", "A Git commit, a base image update, or a timer", "az acr task create"),
                listOf("Multi-step task", "A YAML file of build, run and push steps", "az acr run or a task with --file"),
            ),
            B.p("""
                The base image trigger is the one exams like: when the python:3.12-slim image your Dockerfile starts
                FROM is patched, the task rebuilds your app image automatically, so security fixes reach you without
                anyone remembering to rebuild.
            """),
            B.code("bash", """
                # Quick task: send the folder to Azure, build there, push to the registry
                az acr build --registry ttcoach --image coach-api:{{.Run.ID}} .

                # Triggered task: rebuild on every commit to main (needs a GitHub token)
                az acr task create --registry ttcoach --name coach-ci \
                  --image coach-api:{{.Run.ID}} --context https://github.com/twotracks/coach.git#main \
                  --file Dockerfile --git-access-token <token>
            """),
            B.code("yaml", """
                # acr-task.yaml: build, test, then push only if tests pass
                version: v1.1.0
                steps:
                  - build: -t {{.Run.Registry}}/coach-api:{{.Run.ID}} .
                  - cmd: {{.Run.Registry}}/coach-api:{{.Run.ID}} python -m pytest
                  - push:
                    - {{.Run.Registry}}/coach-api:{{.Run.ID}}
            """),
            B.check(
                "ai-acr-k2", "ai-acr",
                "The team wants images rebuilt whenever the Python base image gets a security patch. What do you set up?",
                listOf("An ACR task with a base image update trigger", "A quick task run by hand each week", "Geo-replication"), 0,
                "A task tracks the base image in the Dockerfile's FROM line and rebuilds when it changes.",
                whyWrong = mapOf(
                    1 to "A quick task only runs when someone remembers to run it.",
                    2 to "Geo-replication copies images between regions; it never rebuilds anything.",
                ),
            ),
            B.h("Signing in without passwords"),
            B.bullets(
                "People: az acr login uses your Entra sign-in and gives Docker a short-lived token.",
                "Apps and hosts: a managed identity with the AcrPull role on the registry pulls images; AcrPush is for pipelines that push.",
                "The admin user is one shared username and password for the whole registry. It is off by default; keep it off outside quick tests.",
            ),
            B.code("python", """
                # List the tags of coach-api with the Python SDK and Entra sign-in
                from azure.identity import DefaultAzureCredential
                from azure.containerregistry import ContainerRegistryClient

                client = ContainerRegistryClient(
                    "https://ttcoach.azurecr.io", DefaultAzureCredential(),
                    audience="https://management.azure.com",
                )
                for tag in client.list_tag_properties("coach-api"):
                    print(tag.name, tag.digest)
            """),
            B.worked("""
                Worked example. Usaid builds coach-api with az acr build and tags it with the run id, ca7. The App
                Service for Two Tracks Coach has a system-assigned identity, and he grants it AcrPull on ttcoach. He
                deploys coach-api:ca7, not latest, so a later push cannot change what is running. The admin user stays
                disabled.
            """),
            B.faded(
                "ai-acr-f1",
                "A new Container App for the coach's worker must pull from ttcoach, with no password stored anywhere.",
                listOf(
                    "Give the Container App a managed identity.",
                    "On the registry, grant that identity the ___ role, and nothing stronger.",
                    "Point the app at the image by a unique tag, and leave the admin user off.",
                ),
                blank = 1,
                answers = listOf("acrpull", "acr pull"),
                note = "Pulling needs AcrPull only. AcrPush is for whatever builds and pushes images.",
            ),
            B.steps("Create a registry and build in the cloud",
                "az acr create --name ttcoach --resource-group tt-rg --sku Basic" to "Admin user is disabled by default.",
                "az acr build --registry ttcoach --image coach-api:v1 ." to "The build log streams to your terminal; no local Docker needed.",
                "az acr repository show-tags --name ttcoach --repository coach-api" to "Shows v1.",
                "az acr repository show-manifests --name ttcoach --repository coach-api" to "Shows the sha256 digest for each image.",
                "az role assignment create --assignee <principalId> --role AcrPull --scope <registry-id>" to "The host's identity can now pull.",
            ),
            B.mistakes(
                "Deploying coach-api:latest and finding a later push quietly changed production.",
                "Turning on the admin user for an app because it is quicker than a managed identity.",
                "Granting AcrPush or Owner to an app that only ever pulls images.",
                "Expecting geo-replication on a Basic or Standard registry; it needs Premium.",
            ),
            B.example("Two Tracks Coach: one Premium registry ttcoach geo-replicated to two regions, images built by an ACR task on every commit and on base image updates, tagged with the run id, pulled with AcrPull by managed identities."),
            B.check(
                "ai-acr-k3", "ai-acr",
                "Why is the admin user a poor choice for App Service pulling from ACR?",
                listOf("It is one shared password with push and pull rights to the whole registry", "It only works with Premium", "It cannot pull images"), 0,
                "A managed identity with AcrPull is scoped, auditable and has no secret to leak or rotate.",
                whyWrong = mapOf(
                    1 to "The admin user exists on every tier.",
                    2 to "It can pull and push; that is part of the problem.",
                ),
            ),
            B.h("Check yourself"),
            B.match(
                "ai-x-acr-match", "Pair each ACR term with what it means.",
                "Tag" to "A movable, friendly label such as 1.4.0",
                "Digest" to "A sha256 fingerprint that never changes",
                "Quick task" to "az acr build: build and push once in Azure",
                "Geo-replication" to "Copies of one registry in several regions",
                "AcrPull" to "The role an app needs to download images",
            ),
            B.gap(
                "ai-x-acr-gap1", "The tier you need for geo-replication and private endpoints.",
                "Geo-replication needs the ___ tier.", "Premium", "premium",
            ),
            B.gap(
                "ai-x-acr-gap2", "The command that builds an image in Azure without local Docker.",
                "Run az acr ___ with a registry, an image name and a folder.", "build",
            ),
            B.tryIt("Try it in 15 minutes", "A Basic registry costs a few cents a day; delete the resource group afterwards. ACR Tasks may not run on free-credit subscriptions.",
                "Create a Basic registry and check the admin user is off",
                "Build a small Python image with az acr build and a unique tag",
                "List its tags and its digest",
                "Push the same tag again and see the digest change while the tag stays",
            ),
            B.explain(
                "ai-acr-x1",
                "Explain how Two Tracks Coach gets a trustworthy image from source code into a running host without any password.",
                "An ACR task builds the image in Azure, either as a quick task with az acr build or triggered by a commit or a base image update, and pushes it to the registry. Each build gets a unique tag, and the digest identifies the exact content, so deploying that tag or digest means a later push cannot change what runs. The host has a managed identity with the AcrPull role, so it signs in with Entra and the admin user stays disabled.",
                "ACR Tasks build the image" to listOf("acr task", "az acr build", "quick task", "triggered", "base image"),
                "Unique tags or digests, not latest" to listOf("digest", "unique tag", "sha256", "immutable"),
                "Managed identity with AcrPull, no admin user" to listOf("acrpull", "managed identity", "admin user", "entra"),
            ),
            B.glossary(
                "Container registry" to "A private store for container images, such as ttcoach.azurecr.io.",
                "Repository" to "All the versions of one image inside a registry.",
                "Tag" to "A movable name for one image version.",
                "Digest" to "The sha256 hash that identifies an image's exact content.",
                "ACR Tasks" to "Cloud builds: quick, triggered or multi-step.",
                "AcrPull" to "The built-in role that allows pulling images and nothing more.",
            ),
            B.recap(
                "Registry, repository, tag and digest; deploy unique tags, because tags move and digests do not.",
                "ACR Tasks build in Azure: az acr build once, triggers for commits, base images and timers, YAML for multi-step.",
                "Pull with a managed identity holding AcrPull; keep the admin user off; Premium for geo-replication.",
            ),
        ),
    )

    private val appService = Topic(
        id = "ai-appservice-containers", title = "Containers on App Service: settings, secrets and slots", minutes = 25,
        references = listOf(
            B.ref("Configure a custom container for App Service", LEARN + "azure/app-service/configure-custom-container"),
            B.ref("Tutorial: Build and run a custom image in App Service", LEARN + "azure/app-service/tutorial-custom-container"),
            B.ref("Configure an App Service app (app settings)", LEARN + "azure/app-service/configure-common"),
            B.ref("Use Key Vault references in App Service", LEARN + "azure/app-service/app-service-key-vault-references"),
            B.ref("Set up staging environments (deployment slots)", LEARN + "azure/app-service/deploy-staging-slots"),
            B.ref("Learn module: Deploy containers to Azure App Service", LEARN + "training/modules/deploy-containers-azure-app-service/"),
        ),
        blocks = listOf(
            B.warmup(
                "ai-appservice-containers-w1", "ai-appservice-containers",
                "Coach's FastAPI container listens on port 8000. How do you think App Service learns that?",
                listOf("An app setting called WEBSITES_PORT", "It reads the Dockerfile from GitHub", "You must change the app to port 80"), 0,
                "The answer is the same tool App Service uses to hand your app any setting at all.",
            ),
            B.plain("""
                App Service is Azure's managed web hosting. Besides code, it can run your own container image: it pulls
                the image from a registry, starts it, and sends web traffic to it. Settings you type into App Service
                arrive inside the container as ordinary environment variables, and secrets can come straight from
                Key Vault.
            """),
            B.diagram(
                caption = "App Service pulls the image with its identity, resolves Key Vault references and hands every setting to the container as an environment variable.",
                nodes = listOf(
                    DNode("acr", "Registry", 0.15f, 0.25f, w = 0.26f, h = 0.24f, kind = NodeKind.CYLINDER, tone = 2),
                    DNode("app", "App Service", 0.5f, 0.25f, w = 0.3f, h = 0.22f, tone = 1, sub = "production slot"),
                    DNode("kv", "Key Vault", 0.85f, 0.25f, w = 0.26f, h = 0.24f, kind = NodeKind.CYLINDER, tone = 2),
                    DNode("stage", "staging slot", 0.5f, 0.7f, w = 0.3f, h = 0.18f),
                    DNode("env", "Env vars in container", 0.85f, 0.7f, w = 0.26f, h = 0.18f, kind = NodeKind.NOTE),
                ),
                edges = listOf(
                    DEdge("acr", "app", label = "pull"), DEdge("kv", "app", label = "reference"),
                    DEdge("app", "env"), DEdge("stage", "app", label = "swap", bidirectional = true),
                ),
                aspect = 0.8f,
            ),
            B.h("Pulling the image with a managed identity"),
            B.p("""
                App Service needs permission to pull from your registry. Give the app a managed identity (an Entra
                identity Azure creates for it), grant that identity AcrPull on the registry, and tell App Service to
                use it for pulls with the site setting acrUseManagedIdentityCreds. For a user-assigned identity you
                also give its client id. No registry password is stored.
            """),
            B.code("bash", """
                az webapp create --resource-group tt-rg --plan tt-plan --name tt-coach \
                  --container-image-name ttcoach.azurecr.io/coach-api:ca7
                az webapp identity assign --resource-group tt-rg --name tt-coach
                az role assignment create --assignee <principalId> --role AcrPull --scope <registry-id>
                az resource update --ids <web-config-id> --set properties.acrUseManagedIdentityCreds=True
            """),
            B.h("Settings become environment variables"),
            B.p("""
                Every app setting you add in App Service is injected into the container as an environment variable
                when it starts. Changing a setting restarts the app. Python reads them with os.environ, so the same
                image runs anywhere with different settings.
            """),
            B.code("python", """
                import os
                from fastapi import FastAPI

                app = FastAPI()
                COSMOS_ENDPOINT = os.environ["COSMOS_ENDPOINT"]      # a plain app setting
                OPENAI_KEY = os.environ["OPENAI_KEY"]                # a Key Vault reference, already resolved

                @app.get("/health")
                def health():
                    return {"ok": True, "region": os.environ.get("REGION_NAME", "unknown")}
            """),
            B.table(
                listOf("Setting", "What it does"),
                listOf("WEBSITES_PORT", "The port your container listens on, when it is not 80 (Coach uses 8000)"),
                listOf("WEBSITES_ENABLE_APP_SERVICE_STORAGE", "Whether /home is shared, persistent storage for the container"),
                listOf("Any name you choose", "Arrives as an environment variable, such as COSMOS_ENDPOINT"),
            ),
            B.check(
                "ai-appservice-containers-k1", "ai-appservice-containers",
                "The Coach container starts, but every request times out. It listens on 8000. What is missing?",
                listOf("The WEBSITES_PORT app setting set to 8000", "The AcrPull role", "A deployment slot"), 0,
                "App Service sends traffic to port 80 unless WEBSITES_PORT says otherwise.",
                whyWrong = mapOf(
                    1 to "Without AcrPull the image would never be pulled, so the container would not start at all.",
                    2 to "Slots help with releases; they do not change which port gets traffic.",
                ),
            ),
            B.h("Secrets: Key Vault references"),
            B.p("""
                Never put a key in the image or as a plain setting. Store it in Key Vault and make the app setting a
                Key Vault reference. App Service reads the secret with the app's managed identity, which needs the Key
                Vault Secrets User role, and the container just sees the value in an environment variable.
            """),
            B.code("text", """
                OPENAI_KEY = @Microsoft.KeyVault(SecretUri=https://tt-vault.vault.azure.net/secrets/OpenAiKey/)
                OPENAI_KEY = @Microsoft.KeyVault(VaultName=tt-vault;SecretName=OpenAiKey)
            """),
            B.remember("Leave the version off the secret URI so the app picks up a rotated secret. If the reference cannot be resolved, the app sees the reference text itself, so check the green tick in the portal."),
            B.h("Slots and continuous deployment"),
            B.bullets(
                "A deployment slot is a second live copy of the app, such as staging, with its own URL. Slots need Standard tier or higher.",
                "Deploy the new image to staging, warm it up, then swap: the slots trade places with no downtime, and swapping back is a rollback.",
                "Most settings move with the swap. Tick deployment slot setting on a setting to keep it stuck to its slot, such as a staging database.",
                "Continuous deployment: turn it on for the app to get a webhook URL, then create an ACR webhook for push events. Each push makes the app pull the new image.",
                "Logs: turn on container logging with az webapp log config --docker-container-logging filesystem, then watch with az webapp log tail.",
            ),
            B.check(
                "ai-appservice-containers-k2", "ai-appservice-containers",
                "Staging must use its own test database even after a swap. What do you do with DB_NAME?",
                listOf("Mark it as a deployment slot setting", "Put it in the Dockerfile", "Make it a Key Vault reference"), 0,
                "Slot settings stay with the slot during a swap, so each slot keeps its own value.",
                whyWrong = mapOf(
                    1 to "A value baked into the image would travel with the image into production.",
                    2 to "A reference changes where the value comes from, not whether it swaps.",
                ),
            ),
            B.worked("""
                Worked example. Usaid deploys coach-api:ca8 to the staging slot of tt-coach. COSMOS_ENDPOINT is a normal
                setting, OPENAI_KEY is a Key Vault reference, and DB_NAME is marked as a slot setting so staging keeps
                coach-test. He checks /health on the staging URL, then swaps. Production now runs ca8 with production's
                DB_NAME, and swapping back would restore ca7.
            """),
            B.faded(
                "ai-appservice-containers-f1",
                "The Coach's Redis host differs between staging and production, and a swap must not mix them up.",
                listOf(
                    "Add REDIS_HOST as an app setting in each slot with its own value.",
                    "Mark REDIS_HOST as a deployment ___ setting so it stays where it is during a swap.",
                    "Swap; the new image moves to production but each slot keeps its own Redis host.",
                ),
                blank = 1,
                answers = listOf("slot"),
                note = "Settings follow the swap unless they are slot settings. Connection details per environment are the usual slot settings.",
            ),
            B.steps("Deploy Coach from ACR to App Service",
                "az appservice plan create --name tt-plan --resource-group tt-rg --is-linux --sku S1" to "A Linux plan; S1 allows slots.",
                "az webapp create ... --container-image-name ttcoach.azurecr.io/coach-api:ca7" to "The app exists and points at the image.",
                "az webapp identity assign, then grant AcrPull and set acrUseManagedIdentityCreds" to "Pulls use the identity, no password.",
                "az webapp config appsettings set --name tt-coach --resource-group tt-rg --settings WEBSITES_PORT=8000" to "Traffic reaches the container.",
                "az webapp log config --name tt-coach --resource-group tt-rg --docker-container-logging filesystem; az webapp log tail ..." to "Container start-up lines and uvicorn output stream in.",
            ),
            B.mistakes(
                "Forgetting WEBSITES_PORT when the container does not listen on port 80.",
                "Storing the OpenAI key as a plain app setting instead of a Key Vault reference.",
                "Giving the app's identity no Key Vault role, so the reference never resolves.",
                "Not marking per-environment settings as slot settings, so a swap points production at test data.",
                "Pushing a new image and expecting App Service to notice without continuous deployment or a restart.",
            ),
            B.example("Two Tracks Coach: Linux S1 plan, image pulled with a system identity holding AcrPull, WEBSITES_PORT=8000, secrets as Key Vault references, staging slot with slot settings, ACR webhook for continuous deployment."),
            B.check(
                "ai-appservice-containers-k3", "ai-appservice-containers",
                "After pushing coach-api:latest, production still runs the old image. Continuous deployment is meant to be on. What do you check?",
                listOf("The ACR webhook pointing at the app's deployment URL", "The WEBSITES_PORT setting", "The Key Vault firewall"), 0,
                "The registry notifies App Service through a webhook on push; without it the app only pulls on restart.",
                whyWrong = mapOf(
                    1 to "The port decides where traffic goes, not when a new image is pulled.",
                    2 to "Key Vault plays no part in pulling images.",
                ),
            ),
            B.h("Check yourself"),
            B.match(
                "ai-x-appsvc-match", "Pair each piece of configuration with its job.",
                "WEBSITES_PORT" to "Tells App Service which port the container listens on",
                "Key Vault reference" to "Fills a setting from a secret at start-up",
                "Slot setting" to "Stays with its slot during a swap",
                "acrUseManagedIdentityCreds" to "Pulls images with the app's identity",
            ),
            B.gap(
                "ai-x-appsvc-gap1", "How the container sees an app setting.",
                "Every app setting arrives as an ___ variable.", "environment", "env",
            ),
            B.gap(
                "ai-x-appsvc-gap2", "What a registry calls on every push to trigger a pull.",
                "Continuous deployment uses an ACR ___ for push events.", "webhook", "webhooks",
            ),
            B.tryIt("Try it in 15 minutes", "A B1 plan costs a little per hour; delete the resource group when done.",
                "Create a Linux web app from a public sample image",
                "Add an app setting and print it from the container's log",
                "Set WEBSITES_PORT to match the image and browse to the site",
                "Turn on container logging and watch az webapp log tail while it restarts",
            ),
            B.explain(
                "ai-appservice-containers-x1",
                "Explain how the Coach container on App Service gets its image, its settings and its secrets without any password in code.",
                "App Service pulls the image from ACR using the app's managed identity, which holds the AcrPull role, after acrUseManagedIdentityCreds is turned on. Every app setting, including WEBSITES_PORT for port 8000, arrives inside the container as an environment variable. Secrets such as the OpenAI key are Key Vault references, which the platform resolves with the same identity, so the code only reads os.environ.",
                "Managed identity with AcrPull pulls the image" to listOf("acrpull", "managed identity", "acrusemanagedidentitycreds"),
                "Settings arrive as environment variables" to listOf("environment variable", "os.environ", "app setting", "websites_port"),
                "Secrets via Key Vault references" to listOf("key vault reference", "key vault", "keyvault"),
            ),
            B.glossary(
                "Custom container" to "Your own image that App Service pulls and runs instead of a built-in stack.",
                "App setting" to "A name and value App Service passes to the app as an environment variable.",
                "WEBSITES_PORT" to "The setting that names the port the container listens on.",
                "Key Vault reference" to "A setting value that App Service resolves from Key Vault at start-up.",
                "Deployment slot" to "A live copy of the app, such as staging, that can be swapped with production.",
                "Slot setting" to "A setting that stays with its slot during a swap.",
            ),
            B.recap(
                "Pull from ACR with a managed identity holding AcrPull; WEBSITES_PORT when the port is not 80.",
                "App settings become environment variables; secrets are Key Vault references resolved with the identity.",
                "Deploy to a staging slot and swap; slot settings stay put; an ACR webhook gives continuous deployment.",
            ),
        ),
    )

    private val containerApps = Topic(
        id = "ai-container-apps", title = "Azure Container Apps: environments, revisions and KEDA scaling", minutes = 28,
        references = listOf(
            B.ref("Azure Container Apps overview", LEARN + "azure/container-apps/overview"),
            B.ref("Azure Container Apps environments", LEARN + "azure/container-apps/environment"),
            B.ref("Update and deploy changes (revisions)", LEARN + "azure/container-apps/revisions"),
            B.ref("Traffic splitting in Azure Container Apps", LEARN + "azure/container-apps/traffic-splitting"),
            B.ref("Scaling in Azure Container Apps", LEARN + "azure/container-apps/scale-app"),
            B.ref("Manage secrets in Azure Container Apps", LEARN + "azure/container-apps/manage-secrets"),
            B.ref("Jobs in Azure Container Apps", LEARN + "azure/container-apps/jobs"),
            B.ref("Learn module: Deploy containers to Azure Container Apps", LEARN + "training/modules/deploy-containers-azure-container-apps/"),
        ),
        blocks = listOf(
            B.warmup(
                "ai-container-apps-w1", "ai-container-apps",
                "Coach's worker reads a Service Bus queue. At night the queue is empty. How many copies do you think should run?",
                listOf("Zero, and start again when messages arrive", "Always at least one", "Ten, to be ready"), 0,
                "You pay per running copy here, and something can watch the queue for you.",
            ),
            B.plain("""
                Azure Container Apps runs your containers without you managing servers or a Kubernetes cluster. You
                give it an image; it runs copies (replicas), scales them up and down, even to zero, and keeps each
                version of your app as a revision you can send traffic to. Under the hood it uses Kubernetes and
                KEDA, but you never touch them directly.
            """),
            B.diagram(
                caption = "Ingress splits traffic between two revisions, and a KEDA rule adds replicas as the Service Bus queue grows.",
                nodes = listOf(
                    DNode("envb", "Container Apps environment", 0.5f, 0.1f, w = 0.9f, h = 0.12f, tone = 2),
                    DNode("ing", "Ingress", 0.5f, 0.32f, w = 0.3f, h = 0.14f, kind = NodeKind.PILL, tone = 1),
                    DNode("r1", "Revision v1", 0.25f, 0.58f, w = 0.32f, h = 0.18f, sub = "80% traffic"),
                    DNode("r2", "Revision v2", 0.75f, 0.58f, w = 0.32f, h = 0.18f, sub = "20%, label beta"),
                    DNode("sb", "Service Bus queue", 0.25f, 0.88f, w = 0.34f, h = 0.2f, kind = NodeKind.CYLINDER, tone = 2),
                    DNode("keda", "KEDA scale rule", 0.75f, 0.88f, w = 0.34f, h = 0.16f, kind = NodeKind.NOTE),
                ),
                edges = listOf(
                    DEdge("ing", "r1", label = "80%"), DEdge("ing", "r2", label = "20%"),
                    DEdge("keda", "sb", label = "polls"), DEdge("keda", "r2", label = "replicas"),
                ),
                aspect = 0.85f,
            ),
            B.h("Environments, ingress, secrets and environment variables"),
            B.bullets(
                "An environment is the boundary around a group of container apps: they share a virtual network and a Log Analytics workspace, and can call each other by name.",
                "Ingress makes an app reachable over HTTP: external for the internet, internal for other apps in the environment only. You set the target port, such as 8000.",
                "Secrets are stored on the app; an environment variable points at one with secretref:. A secret can also be a Key Vault reference read with a managed identity.",
                "Changing a secret does not create a revision; restart the revision so containers see the new value.",
            ),
            B.code("bash", """
                az containerapp env create --name tt-env --resource-group tt-rg --location westeurope

                az containerapp create --name coach-api --resource-group tt-rg --environment tt-env \
                  --image ttcoach.azurecr.io/coach-api:ca8 --registry-server ttcoach.azurecr.io \
                  --registry-identity system --ingress external --target-port 8000 \
                  --secrets "openai-key=keyvaultref:https://tt-vault.vault.azure.net/secrets/OpenAiKey,identityref:<identity-id>" \
                  --env-vars "OPENAI_KEY=secretref:openai-key" "COSMOS_ENDPOINT=https://tt-cosmos.documents.azure.com"
            """),
            B.h("Revisions"),
            B.p("""
                A revision is a frozen snapshot of the app's template: image, environment variables, scale rules.
                Changing any of those creates a new revision. Changing configuration such as secrets, ingress or the
                revision mode applies to every revision and creates none.
            """),
            B.compare("Which revision mode?",
                B.card("Single (default)", "Most apps: always run the newest version.", "One active revision at a time", "New revision gets traffic once it is ready, so no downtime", "Old revision is deactivated for you"),
                B.card("Multiple", "Blue-green, canary and A/B tests.", "Several revisions active together", "Split traffic by percentage", "Labels give one revision its own URL"),
            ),
            B.tip("\"Send 20 percent of users to the new version\" means multiple revision mode with traffic splitting. \"Give testers a stable URL for the new version\" means a label."),
            B.code("bash", """
                az containerapp revision set-mode --name coach-api --resource-group tt-rg --mode multiple
                az containerapp ingress traffic set --name coach-api --resource-group tt-rg \
                  --revision-weight coach-api--v1=80 coach-api--v2=20
                az containerapp revision label add --name coach-api --resource-group tt-rg \
                  --label beta --revision coach-api--v2
            """),
            B.check(
                "ai-container-apps-k1", "ai-container-apps",
                "You rotate the Service Bus secret on coach-worker. Running containers still use the old value. Why?",
                listOf("Secret changes create no revision; the revision must be restarted", "Secrets only apply in multiple revision mode", "Secrets need a label"), 0,
                "Secrets are application-scope configuration; containers read them at start, so restart the revision.",
                whyWrong = mapOf(
                    1 to "Secrets work the same in both revision modes.",
                    2 to "Labels route traffic; they have nothing to do with secrets.",
                ),
            ),
            B.h("Scaling with KEDA"),
            B.p("""
                Each revision scales between a minimum and a maximum number of replicas: by default 0 and 10, up to
                1,000. Scale rules say when to add replicas. Container Apps uses KEDA (Kubernetes Event-driven
                Autoscaling), so a rule can watch HTTP traffic, TCP connections, CPU or memory, or an event source such
                as a Service Bus queue. If any rule's threshold is passed, the app scales out.
            """),
            B.table(
                listOf("Rule", "Scales on", "Example"),
                listOf("HTTP", "Concurrent requests per replica (default 10)", "coach-api: add a replica per 50 requests"),
                listOf("Custom: azure-servicebus", "Messages waiting in a queue or subscription", "coach-worker: one replica per 20 messages"),
                listOf("Custom: CPU or memory", "Resource use; cannot scale to zero", "Heavy embedding jobs"),
            ),
            B.code("bash", """
                az containerapp create --name coach-worker --resource-group tt-rg --environment tt-env \
                  --image ttcoach.azurecr.io/coach-worker:ca8 --user-assigned <identity-id> \
                  --min-replicas 0 --max-replicas 20 \
                  --scale-rule-name embed-queue --scale-rule-type azure-servicebus \
                  --scale-rule-metadata "queueName=embed-jobs" "namespace=tt-bus" "messageCount=20" \
                  --scale-rule-identity <identity-id>
            """),
            B.remember("KEDA polls event sources every 30 seconds by default, and waits a 300-second cool-down after the last event before going from one replica to zero. An app with no ingress and no scale rule scales to zero and never wakes, so give it a rule or min-replicas of 1."),
            B.check(
                "ai-container-apps-k2", "ai-container-apps",
                "coach-worker should cost nothing when the embed-jobs queue is empty and grow with the backlog. What do you configure?",
                listOf("min-replicas 0 and an azure-servicebus scale rule", "min-replicas 1 and a CPU rule", "An HTTP scale rule with ingress"), 0,
                "A queue-based KEDA rule wakes the app from zero when messages wait, and scales out with the queue length.",
                whyWrong = mapOf(
                    1 to "A CPU rule cannot scale to zero, and min-replicas 1 keeps paying all night.",
                    2 to "The worker gets work from a queue, not from HTTP requests.",
                ),
            ),
            B.p("""
                Container Apps jobs are for work that runs and then ends: manual jobs you start, scheduled jobs on a
                cron timer, and event-driven jobs that start an execution per batch of queue messages. A nightly
                re-embedding of all notes is a scheduled job, not an app.
            """),
            B.worked("""
                Worked example. Usaid ships coach-api v2 with a new retrieval prompt. The app is in multiple revision
                mode, so v2 comes up beside v1. He labels v2 as beta and gives the URL to Sanya, then sets traffic to
                v1=80 and v2=20. The numbers look good, so he moves to 0 and 100, and deactivates v1 later.
            """),
            B.faded(
                "ai-container-apps-f1",
                "A new summariser revision should get 10 percent of real users before everyone gets it.",
                listOf(
                    "Single mode would give the new revision all traffic as soon as it is ready.",
                    "Switch the app to ___ revision mode so both revisions stay active.",
                    "Set traffic weights to 90 and 10, then move to 100 when the metrics look right.",
                ),
                blank = 1,
                answers = listOf("multiple"),
                note = "Traffic splitting needs multiple revision mode (a separate labels mode is still in preview); single mode always moves everything to the newest ready revision.",
            ),
            B.steps("Run Coach on Container Apps",
                "az extension add --name containerapp --upgrade" to "The CLI commands for Container Apps.",
                "az containerapp env create --name tt-env --resource-group tt-rg --location westeurope" to "An environment with a Log Analytics workspace.",
                "az containerapp create ... --ingress external --target-port 8000" to "Prints the app's FQDN; browse to /health.",
                "az containerapp revision list --name coach-api --resource-group tt-rg -o table" to "One revision, active, with 100 percent traffic.",
                "az containerapp update --name coach-api --resource-group tt-rg --set-env-vars MODEL=gpt-4o-mini" to "An env var change creates a new revision.",
            ),
            B.mistakes(
                "Expecting traffic splitting while the app is still in single revision mode.",
                "Rotating a secret and not restarting the revision, so containers keep the old value.",
                "Scaling a queue worker on CPU, which can never scale it to zero.",
                "Leaving an app with no ingress and no scale rule at min-replicas 0, so it never starts.",
                "Storing the Service Bus connection string in plain env vars instead of a secret or managed identity.",
            ),
            B.example("Two Tracks Coach: coach-api with external ingress and an HTTP rule, coach-worker scaled from zero by an azure-servicebus rule using a managed identity, secrets as Key Vault references, and a scheduled job for nightly re-embedding."),
            B.check(
                "ai-container-apps-k3", "ai-container-apps",
                "Which change to coach-api creates a new revision?",
                listOf("A new container image tag", "A new secret value", "New traffic weights"), 0,
                "The image is part of the revision template; secrets and traffic are app configuration and create none.",
                whyWrong = mapOf(
                    1 to "Secrets are application-scope; they need a restart, not a new revision.",
                    2 to "Traffic weights are ingress configuration and apply across revisions.",
                ),
            ),
            B.h("Check yourself"),
            B.match(
                "ai-x-aca-match", "Pair each Container Apps term with its meaning.",
                "Environment" to "Shared network and logs for a group of apps",
                "Revision" to "An immutable snapshot of the app's template",
                "Label" to "A stable URL for one specific revision",
                "KEDA" to "The engine that scales replicas on events",
                "Job" to "Container work that runs to completion",
            ),
            B.gap(
                "ai-x-aca-gap1", "How an environment variable points at a stored secret.",
                "Set OPENAI_KEY=___:openai-key.", "secretref",
            ),
            B.gap(
                "ai-x-aca-gap2", "The lowest replica count that costs nothing while idle.",
                "Set min-replicas to ___ for scale to zero.", "0", "zero",
            ),
            B.tryIt("Try it in 15 minutes", "Consumption pricing has a monthly free grant; delete the resource group afterwards.",
                "Create an environment and a container app from a sample image with external ingress",
                "Change an environment variable and list the revisions",
                "Switch to multiple revision mode and split traffic 50/50",
                "Set min-replicas to 0, wait, and watch the replica count drop",
            ),
            B.explain(
                "ai-container-apps-x1",
                "Explain how Container Apps lets Two Tracks Coach release a new version safely and keep the queue worker cheap.",
                "Each change to the image or template creates a new revision. In multiple revision mode the old and new revisions run together, traffic splitting sends a small percentage to the new one, and a label gives testers its own URL; rolling back is moving the traffic. The worker uses a KEDA scale rule on the Service Bus queue with min-replicas 0, so it scales to zero when the queue is empty and adds replicas as messages wait.",
                "Revisions and multiple mode" to listOf("revision", "multiple revision", "multiple mode"),
                "Traffic splitting or labels" to listOf("traffic splitting", "percentage", "label", "weights"),
                "KEDA queue rule and scale to zero" to listOf("keda", "scale rule", "scale to zero", "min-replicas", "service bus"),
            ),
            B.glossary(
                "Environment" to "The boundary that groups container apps with a shared network and log workspace.",
                "Replica" to "One running copy of a revision.",
                "Revision" to "An immutable snapshot of an app's image, settings and scale rules.",
                "Traffic splitting" to "Sending set percentages of requests to different revisions.",
                "KEDA" to "Kubernetes Event-driven Autoscaling, which scales replicas on HTTP or event sources.",
                "secretref" to "The prefix that makes an environment variable read a stored secret.",
                "Container Apps job" to "A container that runs to completion: manual, scheduled or event-driven.",
            ),
            B.recap(
                "An environment groups apps; ingress exposes them; secrets are referenced with secretref and can come from Key Vault.",
                "Template changes create revisions; multiple mode allows traffic splitting and labels; secret changes need a restart.",
                "KEDA rules scale on HTTP or queues such as Service Bus, between min and max replicas, down to zero.",
            ),
        ),
    )

    val topics: List<Topic> = listOf(acr, appService, containerApps)
}
