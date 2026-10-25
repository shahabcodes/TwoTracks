package com.shahaabapps.core.content

/** AI-200 topics, written from the official study guide and Microsoft Learn (Ai200). */
internal object Ai200Containers2 {
    private const val LEARN = "https://learn.microsoft.com/en-us/"

    private val aks = Topic(
        id = "ai-aks", title = "Azure Kubernetes Service: deploy with manifest files", minutes = 28,
        references = listOf(
            B.ref("What is Azure Kubernetes Service (AKS)?", LEARN + "azure/aks/what-is-aks"),
            B.ref("Core Kubernetes concepts for AKS", LEARN + "azure/aks/core-aks-concepts"),
            B.ref("Tutorial: Deploy an application to AKS", LEARN + "azure/aks/tutorial-kubernetes-deploy-application"),
            B.ref("Services in AKS", LEARN + "azure/aks/concepts-network-services"),
            B.ref("Integrate Azure Container Registry with AKS", LEARN + "azure/aks/cluster-container-registry-integration"),
            B.ref("Use Microsoft Entra Workload ID on AKS", LEARN + "azure/aks/workload-identity-overview"),
            B.ref("Comparing Container Apps with other Azure container options", LEARN + "azure/container-apps/compare-options"),
            B.ref("Learn module: Deploy applications to Azure Kubernetes Service", LEARN + "training/modules/deploy-apps-azure-kubernetes-service/"),
        ),
        blocks = listOf(
            B.warmup(
                "ai-aks-w1", "ai-aks",
                "You want three copies of the Coach API always running on AKS. What do you write?",
                listOf("A Deployment manifest with replicas: 3", "Three kubectl run commands", "A Dockerfile with three CMD lines"), 0,
                "In Kubernetes you describe what you want and the cluster keeps it true.",
            ),
            B.plain("""
                Azure Kubernetes Service (AKS) is a Kubernetes cluster that Azure runs half of for you. Azure manages
                the control plane, the brain that stores what should be running. You get the nodes, the virtual machines
                your containers run on. You tell the cluster what you want in YAML files called manifests, send them
                with kubectl apply -f, and Kubernetes keeps reality matching the file: if a container dies, it starts another.
            """),
            B.diagram(
                caption = "A manifest goes to the managed control plane, which runs a Deployment's pods on the nodes; the Service gives them one address and the images come from ACR.",
                nodes = listOf(
                    DNode("dev", "kubectl apply -f coach.yaml", 0.2f, 0.1f, w = 0.36f, h = 0.14f, tone = 1),
                    DNode("api", "Control plane", 0.72f, 0.1f, w = 0.4f, h = 0.16f, tone = 2, sub = "managed by Azure"),
                    DNode("deploy", "Deployment", 0.26f, 0.4f, w = 0.4f, h = 0.16f, sub = "replicas: 3, image v2"),
                    DNode("svc", "Service", 0.78f, 0.4f, w = 0.36f, h = 0.16f, tone = 1, sub = "LoadBalancer, port 80"),
                    DNode("pods", "3 pods on 2 nodes", 0.26f, 0.68f, w = 0.4f, h = 0.16f, sub = "coach-api container"),
                    DNode("cfg", "ConfigMap and Secret", 0.78f, 0.68f, w = 0.36f, h = 0.16f, kind = NodeKind.NOTE, tone = 2),
                    DNode("acr", "ACR: ttcoach.azurecr.io", 0.26f, 0.91f, w = 0.46f, h = 0.14f, kind = NodeKind.CYLINDER, tone = 2),
                ),
                edges = listOf(
                    DEdge("dev", "api", label = "manifest"),
                    DEdge("api", "deploy"),
                    DEdge("deploy", "pods"),
                    DEdge("svc", "pods", label = "label app=coach-api"),
                    DEdge("cfg", "pods", label = "env vars"),
                    DEdge("acr", "pods", label = "AcrPull"),
                ),
                aspect = 1.0f,
            ),
            B.h("The pieces, in plain words"),
            B.table(
                listOf("Thing", "Plain words", "Two Tracks Coach"),
                listOf("Cluster", "The whole Kubernetes setup: control plane plus nodes", "tt-aks"),
                listOf("Node pool", "A group of identical VMs; a system pool for Kubernetes' own parts, user pools for your apps", "A user pool of two small VMs"),
                listOf("Pod", "The smallest thing Kubernetes runs: one or more containers sharing a network address", "One copy of the FastAPI container"),
                listOf("Deployment", "Keeps a number of identical pods running and replaces them in a rolling update", "coach-api, replicas: 3"),
                listOf("Service", "A stable name and address in front of pods, found by their labels", "coach-api, type LoadBalancer"),
                listOf("ConfigMap / Secret", "Settings and sensitive values handed to pods as env vars or files", "MODEL_NAME, SERVICEBUS_CONN"),
            ),
            B.p("""
                Pods come and go, and each new one gets a new IP address. That is why nobody calls a pod directly. A
                Service picks its pods with a label selector, such as app: coach-api, and gives them one address that never
                changes. ClusterIP (the default) is reachable only inside the cluster; LoadBalancer asks Azure for a public
                or internal IP from outside.
            """),
            B.check(
                "ai-aks-k1", "ai-aks",
                "The Coach worker pods are replaced during an update and get new IP addresses. How does the API still reach them?",
                listOf("Through a Service that selects them by label", "By storing pod IPs in a ConfigMap", "By pinning each pod to one node"), 0,
                "A Service gives a stable address and finds the current pods with its label selector.",
                whyWrong = mapOf(
                    1 to "The IPs would be stale the moment a pod restarts; nothing updates the ConfigMap for you.",
                    2 to "Pinning to a node does not keep an IP. A replaced pod still gets a new one.",
                ),
            ),
            B.h("One manifest file for the Coach API"),
            B.p("A manifest can hold several objects, separated by three dashes. This one is a Deployment and a Service. The image comes from the team's registry; the settings come from a ConfigMap and a Secret."),
            B.code("yaml", """
                apiVersion: apps/v1
                kind: Deployment
                metadata:
                  name: coach-api
                spec:
                  replicas: 3
                  selector:
                    matchLabels:
                      app: coach-api
                  template:
                    metadata:
                      labels:
                        app: coach-api
                    spec:
                      containers:
                      - name: coach
                        image: ttcoach.azurecr.io/coach-api:v2
                        ports:
                        - containerPort: 8000
                        envFrom:
                        - configMapRef:
                            name: coach-settings
                        - secretRef:
                            name: coach-secrets
                        resources:
                          requests: { cpu: 250m, memory: 256Mi }
                          limits: { cpu: 500m, memory: 512Mi }
                ---
                apiVersion: v1
                kind: Service
                metadata:
                  name: coach-api
                spec:
                  type: LoadBalancer
                  selector:
                    app: coach-api
                  ports:
                  - port: 80
                    targetPort: 8000
            """),
            B.bullets(
                "selector.matchLabels must match the labels in the pod template, or the Deployment is rejected.",
                "The Service selector must match the same labels, or it has no pods to send traffic to.",
                "targetPort is the port the container listens on; port is what callers use.",
                "Requests tell the scheduler how much a pod needs; limits are the ceiling it may use.",
            ),
            B.tip("A Kubernetes Secret is only base64 encoded, not encrypted in the file. Never commit one to Git. For real secrets, keep them in Key Vault and mount them with the Key Vault secrets provider add-on, or have the code read them with workload identity."),
            B.h("Rollouts and rollbacks"),
            B.p("""
                Change the image tag in the file to v3 and run kubectl apply -f again. The Deployment starts new pods and
                stops old ones a few at a time, so the app never goes fully down; this is a rolling update. kubectl rollout
                status deployment/coach-api waits until it finishes. If v3 is broken, kubectl rollout undo deployment/coach-api
                goes back to the previous version, and kubectl rollout history lists the versions you can return to.
            """),
            B.check(
                "ai-aks-k2", "ai-aks",
                "Version v3 of the Coach API returns errors right after a rollout. What is the fastest way back to v2?",
                listOf("kubectl rollout undo deployment/coach-api", "Delete the Deployment and create it again", "Scale the node pool down and up"), 0,
                "rollout undo returns the Deployment to its previous revision with another rolling update.",
                whyWrong = mapOf(
                    1 to "Deleting takes the whole app offline, and you would still have to fix the image tag first.",
                    2 to "Nodes are machines. Changing their count does nothing to which image the pods run.",
                ),
            ),
            B.h("Pulling from ACR, and talking to Azure"),
            B.p("""
                The nodes must be allowed to pull your private images. az aks update --attach-acr ttcoach grants the AcrPull
                role on the registry to the cluster's kubelet identity, the managed identity the nodes use. No pull secret
                needed. Your code is a separate matter: to read Cosmos DB or Key Vault without a password, use workload
                identity. You enable the OIDC issuer and workload identity on the cluster, create a user-assigned managed
                identity, link it to a Kubernetes service account with a federated credential, annotate that service account
                with the identity's client ID, and label the pod azure.workload.identity/use: "true". In Python,
                DefaultAzureCredential then picks it up with no code change.
            """),
            B.code("yaml", """
                apiVersion: v1
                kind: ServiceAccount
                metadata:
                  name: coach-sa
                  annotations:
                    azure.workload.identity/client-id: <client-id-of-the-managed-identity>
                # In the Deployment's pod template:
                #   metadata.labels:  azure.workload.identity/use: "true"
                #   spec.serviceAccountName: coach-sa
            """),
            B.compare("AKS or Container Apps?",
                B.card("Azure Container Apps", "You want containers and scaling without running Kubernetes.", "No cluster or nodes to manage", "Revisions, ingress and KEDA scaling built in", "Can scale to zero"),
                B.card("AKS", "You need the Kubernetes API itself, or full control.", "Direct access to kubectl and every Kubernetes object", "Your own node pools, including GPU nodes", "You patch, upgrade and size the cluster"),
            ),
            B.check(
                "ai-aks-k3", "ai-aks",
                "Pods show ImagePullBackOff for ttcoach.azurecr.io/coach-api:v2, and the tag exists. What is the likely fix?",
                listOf("az aks update --attach-acr ttcoach", "Add a readiness probe", "Change the Service to LoadBalancer"), 0,
                "Attaching the registry gives the kubelet identity AcrPull, so the nodes can pull private images.",
                whyWrong = mapOf(
                    1 to "A probe runs only after the container has started. Here the image never arrived.",
                    2 to "The Service type decides how traffic reaches pods, not whether the nodes can pull an image.",
                ),
            ),
            B.worked("""
                Worked example. The Coach team ships v3 by changing the image line in coach.yaml and running kubectl apply -f
                coach.yaml. kubectl rollout status deployment/coach-api shows new pods coming up, but users report wrong
                answers. Usaid runs kubectl rollout undo deployment/coach-api; the Deployment rolls back to the v2 pods
                and the errors stop. He then reverts the tag in coach.yaml too, so the file and the cluster agree again.
            """),
            B.faded(
                "ai-aks-f1",
                "The nightly embedding worker, a second Deployment called embed-worker, was updated to v5 and now crashes. You want v4 back.",
                listOf(
                    "Confirm the problem with kubectl rollout status deployment/embed-worker.",
                    "Run kubectl rollout ___ deployment/embed-worker to return to the previous revision.",
                    "Put the v4 tag back in the manifest so the next apply does not bring v5 back.",
                ),
                blank = 1,
                answers = listOf("undo"),
                note = "rollout undo goes back one revision; the file is still the source of truth, so fix it too.",
            ),
            B.steps("Deploy the Coach API to AKS",
                "az aks create --resource-group tt-rg --name tt-aks --node-count 2 --attach-acr ttcoach --enable-oidc-issuer --enable-workload-identity --generate-ssh-keys" to "A cluster with two nodes that can pull from the registry.",
                "az aks get-credentials --resource-group tt-rg --name tt-aks" to "kubectl now talks to tt-aks.",
                "kubectl create configmap coach-settings --from-literal=MODEL_NAME=gpt-4o-mini" to "configmap/coach-settings created.",
                "kubectl apply -f coach.yaml" to "deployment.apps/coach-api created and service/coach-api created.",
                "kubectl rollout status deployment/coach-api" to "successfully rolled out, with 3 of 3 pods ready.",
                "kubectl get service coach-api --watch" to "EXTERNAL-IP changes from pending to a public address.",
            ),
            B.mistakes(
                "Labels in the Service selector that do not match the pod template, so the Service has no endpoints.",
                "Committing a Kubernetes Secret manifest to Git, believing base64 is encryption.",
                "Using the latest tag, so a rollout or rollback cannot tell versions apart.",
                "Fixing a bad release with rollout undo and forgetting to fix the manifest, so the next apply breaks it again.",
                "Choosing AKS for a simple API when Container Apps would run it with no cluster to manage.",
            ),
            B.example("Two Tracks Coach: the API runs as a three-replica Deployment behind a LoadBalancer Service, the embedding worker runs on a separate user node pool, images come from ttcoach through --attach-acr, and the pods read Cosmos DB with workload identity."),
            B.h("Check yourself"),
            B.match(
                "ai-x-aks-match", "Pair each Kubernetes object with its job.",
                "Pod" to "One or more containers sharing a network address",
                "Deployment" to "Keeps a number of identical pods and rolls out updates",
                "Service" to "A stable address that finds pods by label",
                "ConfigMap" to "Plain settings handed to pods",
                "Node pool" to "A group of identical VMs that run the pods",
            ),
            B.gap(
                "ai-x-aks-gap1", "Send a manifest file to the cluster, creating its objects or updating them if they already exist.",
                "kubectl ___ -f coach.yaml", "apply",
            ),
            B.gap(
                "ai-x-aks-gap2", "The role the kubelet identity needs on the registry.",
                "Attaching ACR grants the ___ role.", "AcrPull", "acrpull",
            ),
            B.tryIt("Try it in 15 minutes", "A small cluster costs money by the hour, so delete the resource group when you finish.",
                "Create a two-node AKS cluster and run az aks get-credentials",
                "Write a Deployment and a Service for a public sample image and kubectl apply -f it",
                "Change the image tag, apply again, and watch kubectl rollout status",
                "Run kubectl rollout undo and check the old pods return",
                "Delete the resource group",
            ),
            B.explain(
                "ai-aks-x1",
                "Explain how the Coach API gets onto AKS from a manifest file and how you would recover from a bad release.",
                "You describe the desired state in a YAML manifest: a Deployment that keeps three replicas of the coach-api pod running from the image in ACR, and a Service that finds those pods by label and gives them one stable address. kubectl apply -f sends the manifest to the control plane, which schedules the pods on the nodes. A new image tag applied the same way causes a rolling update; if it is broken, kubectl rollout undo returns the Deployment to the previous revision.",
                "A manifest describes desired state, sent with kubectl apply" to listOf("manifest", "yaml", "kubectl apply", "desired state"),
                "A Deployment keeps replicas, a Service gives a stable address" to listOf("deployment", "replicas", "service", "label"),
                "Rollback with rollout undo" to listOf("rollout undo", "rollback", "roll back", "previous revision"),
            ),
            B.glossary(
                "Manifest" to "A YAML file describing Kubernetes objects and their desired state.",
                "Pod" to "The smallest unit Kubernetes runs: one or more containers.",
                "Deployment" to "An object that keeps a set of identical pods and rolls out changes.",
                "Service" to "A stable network address in front of pods, chosen by label.",
                "Node pool" to "A group of identical VMs in an AKS cluster.",
                "Kubelet identity" to "The managed identity the nodes use, for example to pull from ACR.",
                "Workload identity" to "Lets a pod use an Entra managed identity through its service account.",
            ),
            B.recap(
                "Manifests describe pods, Deployments, Services, ConfigMaps and Secrets; kubectl apply -f makes it so.",
                "A rolling update follows a new image tag; rollout status watches it and rollout undo reverses it.",
                "--attach-acr gives the nodes AcrPull; workload identity gives the code an Entra identity; Container Apps when you do not need Kubernetes itself.",
            ),
        ),
    )

    private val troubleshoot = Topic(
        id = "ai-container-troubleshoot", title = "Troubleshooting AKS and Container Apps: logs, events and connectivity", minutes = 28,
        references = listOf(
            B.ref("Troubleshooting in Azure Container Apps", LEARN + "azure/container-apps/troubleshooting"),
            B.ref("View log streams in Azure Container Apps", LEARN + "azure/container-apps/log-streaming"),
            B.ref("Monitor logs in Azure Container Apps with Log Analytics", LEARN + "azure/container-apps/log-monitoring"),
            B.ref("Connect to a container console in Azure Container Apps", LEARN + "azure/container-apps/container-console"),
            B.ref("Health probes in Azure Container Apps", LEARN + "azure/container-apps/health-probes"),
            B.ref("Monitor Azure Kubernetes Service", LEARN + "azure/aks/monitor-aks"),
            B.ref("Kubernetes monitoring in Azure Monitor (Container insights)", LEARN + "azure/azure-monitor/containers/kubernetes-monitoring-overview"),
            B.ref("Azure Kubernetes Service troubleshooting documentation", LEARN + "troubleshoot/azure/azure-kubernetes/welcome-azure-kubernetes"),
        ),
        blocks = listOf(
            B.warmup(
                "ai-container-troubleshoot-w1", "ai-container-troubleshoot",
                "A Coach pod is stuck in CrashLoopBackOff. Where would you look first?",
                listOf("The logs of the container that just crashed", "The ACR repository list", "The Service's external IP"), 0,
                "The container started and died. Something must say why it died.",
            ),
            B.plain("""
                When a container misbehaves, ask three questions in order. Did it start? The status and the events say.
                Why did it stop? Its own logs say. Can traffic reach it? Probes, ingress and the Service say. AKS answers
                with kubectl; Container Apps answers with revision status, system and console logs, and a console you can
                open inside the container.
            """),
            B.diagram(
                caption = "Two ladders that ask the same questions: what state is it in, what did the platform see, what did the app print, and what was kept for later.",
                nodes = listOf(
                    DNode("get", "AKS: kubectl get pods", 0.26f, 0.1f, w = 0.46f, h = 0.14f),
                    DNode("describe", "kubectl describe pod", 0.26f, 0.35f, w = 0.46f, h = 0.16f, sub = "Events: pull, probe, OOM"),
                    DNode("logs", "kubectl logs --previous", 0.26f, 0.6f, w = 0.46f, h = 0.14f),
                    DNode("ci", "Container insights", 0.26f, 0.86f, w = 0.46f, h = 0.16f, kind = NodeKind.CYLINDER, tone = 2, sub = "ContainerLogV2, KubeEvents"),
                    DNode("rev", "Container Apps: revision list", 0.75f, 0.1f, w = 0.44f, h = 0.14f, tone = 1),
                    DNode("sys", "System log stream", 0.75f, 0.35f, w = 0.44f, h = 0.16f, tone = 1, sub = "pull, probes, provisioning"),
                    DNode("con", "Console log stream", 0.75f, 0.6f, w = 0.44f, h = 0.14f, tone = 1, sub = "your app's stdout"),
                    DNode("la", "Log Analytics", 0.75f, 0.86f, w = 0.44f, h = 0.16f, kind = NodeKind.CYLINDER, tone = 2, sub = "ContainerAppConsoleLogs_CL"),
                ),
                edges = listOf(
                    DEdge("get", "describe"), DEdge("describe", "logs"), DEdge("logs", "ci"),
                    DEdge("rev", "sys"), DEdge("sys", "con"), DEdge("con", "la"),
                ),
                aspect = 1.0f,
            ),
            B.h("AKS: read the status, then the events"),
            B.p("kubectl get pods shows each pod's STATUS, READY count and RESTARTS. The status tells you which question to ask next. kubectl describe pod <name> ends with an Events list, the platform's own diary: scheduling, pulling, probe failures, kills. kubectl get events --sort-by=.lastTimestamp shows the same diary for the whole namespace."),
            B.table(
                listOf("What you see", "What it means", "First move"),
                listOf("Pending", "No node has room for the pod's requests, or a volume is not ready", "describe: look for FailedScheduling"),
                listOf("ImagePullBackOff / ErrImagePull", "Wrong image name or tag, or no permission to pull", "describe; check the tag, run az aks check-acr"),
                listOf("CrashLoopBackOff", "The container starts, exits, and Kubernetes keeps retrying with a growing delay", "kubectl logs --previous"),
                listOf("Running but READY 0/1", "The readiness probe is failing, so the Service sends it no traffic", "describe: look for probe failures"),
                listOf("OOMKilled, restarts rising", "The container went over its memory limit", "raise the limit or fix the leak"),
            ),
            B.code("bash", """
                kubectl get pods -l app=coach-api
                kubectl describe pod coach-api-7d9f8c6b5-x2kqp
                kubectl logs coach-api-7d9f8c6b5-x2kqp --previous
                kubectl logs deployment/coach-api --follow
                kubectl get events --sort-by=.lastTimestamp
            """),
            B.tip("--previous is the key to CrashLoopBackOff. Plain kubectl logs shows the container that is starting now, which may have printed nothing yet. --previous shows the one that just died, with its stack trace."),
            B.check(
                "ai-container-troubleshoot-k1", "ai-container-troubleshoot",
                "A pod shows ImagePullBackOff. Which command shows you the exact pull error?",
                listOf("kubectl describe pod", "kubectl logs", "kubectl get service"), 0,
                "The pull error is a platform event, so it appears in the Events section of describe.",
                whyWrong = mapOf(
                    1 to "The container never started, so it has no logs to show.",
                    2 to "Services are about traffic. The image problem happens before any traffic.",
                ),
            ),
            B.h("Probes: alive, ready, started"),
            B.p("""
                A probe is a check the platform runs against your container. A liveness probe failing means the container
                is stuck, so it is restarted. A readiness probe failing means not now, so it is taken out of the traffic but
                left running. A startup probe gives a slow starter time before the other two begin. Both AKS and Container
                Apps support all three. A wrong path or port in a probe is one of the commonest causes of a healthy app
                that never gets traffic, or that is restarted forever.
            """),
            B.code("python", """
                # FastAPI: two cheap endpoints for the probes
                from fastapi import FastAPI, Response

                app = FastAPI()
                model_loaded = False

                @app.get("/healthz")          # liveness: the process answers
                def healthz():
                    return {"status": "ok"}

                @app.get("/ready")            # readiness: dependencies are ready
                def ready(response: Response):
                    if not model_loaded:
                        response.status_code = 503
                    return {"ready": model_loaded}
            """),
            B.check(
                "ai-container-troubleshoot-k2", "ai-container-troubleshoot",
                "The Coach API pods are Running, never restart, and show READY 0/1. Callers get no response. What is failing?",
                listOf("The readiness probe", "The liveness probe", "The image pull"), 0,
                "A failing readiness probe keeps the pod alive but out of the Service's endpoints.",
                whyWrong = mapOf(
                    1 to "A failing liveness probe restarts the container, and these pods never restart.",
                    2 to "The pods are Running, so the image arrived.",
                ),
            ),
            B.h("Container Apps: revisions, replicas and logs"),
            B.p("""
                In Container Apps, start with az containerapp revision list: each revision has a provisioning status and a
                running status. A revision stuck provisioning, or Degraded with 0/1 replicas ready, usually means a probe or
                the target port is wrong. az containerapp replica list shows the replicas of one revision. Then read the
                logs. System logs come from the platform (image pulls, probe results, revision events); console logs are
                your app's stdout and stderr. Stream either live, or query them later in Log Analytics.
            """),
            B.code("bash", """
                az containerapp revision list -n coach-api -g tt-rg -o table
                az containerapp replica list -n coach-api -g tt-rg --revision coach-api--v7 -o table
                az containerapp logs show -n coach-api -g tt-rg --type system --follow
                az containerapp logs show -n coach-api -g tt-rg --type console --tail 50
                az containerapp exec -n coach-api -g tt-rg --command /bin/sh
            """),
            B.code("kql", """
                ContainerAppConsoleLogs_CL
                | where ContainerAppName_s == "coach-api" and TimeGenerated > ago(1h)
                | project TimeGenerated, RevisionName_s, Log_s
                | order by TimeGenerated desc

                // AKS with Container insights: recent warnings from Kubernetes
                KubeEvents
                | where TimeGenerated > ago(1h) and KubeEventType == "Warning"
                | project TimeGenerated, Name, Reason, Message
            """),
            B.remember("Log stream is live and short-lived. Log Analytics keeps history you can query with KQL: ContainerAppConsoleLogs_CL and ContainerAppSystemLogs_CL for Container Apps, ContainerLogV2 and KubeEvents for AKS once Container insights is on."),
            B.h("End-to-end connectivity"),
            B.bullets(
                "AKS: kubectl get endpoints coach-api. Empty means the Service selector matches no ready pod.",
                "AKS: kubectl port-forward deployment/coach-api 8080:8000 tests the pod directly, skipping the Service.",
                "AKS: kubectl exec into a pod and curl another Service by its DNS name to test inside the cluster.",
                "Container Apps: check ingress is enabled, external or internal as intended, and the target port matches the port the app listens on.",
                "Container Apps: az containerapp exec opens a shell in a running replica to test a dependency from where the app sits.",
            ),
            B.check(
                "ai-container-troubleshoot-k3", "ai-container-troubleshoot",
                "A new Container Apps revision shows Running status Degraded, 0/1 replicas ready. The app listens on 8000 but ingress targets 80. What is wrong?",
                listOf("The target port does not match, so the default probes fail", "The image tag is missing in ACR", "Log Analytics is not connected"), 0,
                "With ingress on, the default probes check the target port. Nothing answers on 80, so no replica becomes ready.",
                whyWrong = mapOf(
                    1 to "A missing image fails at the pull, before any replica runs; the system log would say so.",
                    2 to "Logging affects what you can see, not whether a replica becomes ready.",
                ),
            ),
            B.worked("""
                Worked example. After a release, coach-api pods on AKS show ImagePullBackOff. kubectl describe pod shows the
                event "manifest for ttcoach.azurecr.io/coach-api:v31 not found". The pipeline pushed v3.1, and the manifest
                says v31. Usaid fixes the tag in coach.yaml, runs kubectl apply -f, and kubectl get pods shows Running 1/1.
                The event named the exact cause; the logs could not have, because the container never started.
            """),
            B.faded(
                "ai-container-troubleshoot-f1",
                "The embedding worker on AKS shows CrashLoopBackOff and RESTARTS 6. kubectl logs shows nothing useful because the new container has only just started.",
                listOf(
                    "kubectl get pods confirms the CrashLoopBackOff status.",
                    "kubectl logs embed-worker-5c7d --___ shows the output of the container that just crashed.",
                    "Its stack trace says the COSMOS_ENDPOINT variable is missing, so add it to the ConfigMap and apply again.",
                ),
                blank = 1,
                answers = listOf("previous"),
                note = "A crash leaves its story in the previous container's logs. Events tell you about pulls and probes; logs tell you about your code.",
            ),
            B.steps("Diagnose a sick Coach API",
                "kubectl get pods -l app=coach-api" to "STATUS, READY and RESTARTS for every pod.",
                "kubectl describe pod <name>" to "The Events list: pulls, scheduling, probe failures.",
                "kubectl logs <name> --previous" to "The last crash's output, if it crashed.",
                "kubectl get endpoints coach-api" to "The pod IPs behind the Service; empty means no ready pod matches.",
                "az containerapp logs show -n coach-api -g tt-rg --type system" to "For the Container Apps copy: pull, probe and revision messages.",
                "az containerapp revision list -n coach-api -g tt-rg -o table" to "Which revision is active, and whether it is healthy.",
            ),
            B.mistakes(
                "Reading kubectl logs for an ImagePullBackOff, when the container never ran and only describe has the answer.",
                "Running kubectl logs without --previous on a crash loop and seeing an empty new container.",
                "Pointing a liveness probe at a check that calls the database, so a slow database restarts every pod.",
                "Setting the Container Apps target port to 80 while the app listens on 8000.",
                "Looking for yesterday's errors in the log stream instead of querying Log Analytics.",
            ),
            B.example("Two Tracks Coach: the API on Container Apps sends console and system logs to Log Analytics, the worker on AKS has Container insights on, both have /healthz and /ready probes, and the on-call note starts with get, describe, logs --previous."),
            B.h("Check yourself"),
            B.match(
                "ai-x-trouble-match", "Pair each symptom with its first move.",
                "ImagePullBackOff" to "kubectl describe pod, then check the tag and AcrPull",
                "CrashLoopBackOff" to "kubectl logs --previous",
                "Running but READY 0/1" to "Check the readiness probe's path and port",
                "Pending" to "Look for FailedScheduling in the events",
                "Revision Degraded in Container Apps" to "Read the system logs and check the target port",
            ),
            B.gap(
                "ai-x-trouble-gap1", "The command whose output ends with the pod's Events.",
                "kubectl ___ pod coach-api-7d9f", "describe",
            ),
            B.gap(
                "ai-x-trouble-gap2", "Container Apps logs that come from your own code's stdout.",
                "Stream them with --type ___", "console",
            ),
            B.tryIt("Try it in 15 minutes", "Use any container app or AKS cluster you already have, or the one from the previous topic.",
                "Deploy a revision with a wrong target port and watch it go Degraded",
                "Stream the system logs and find the probe failure",
                "Fix the port and confirm the revision becomes healthy",
                "Open a console with az containerapp exec and curl the app on localhost",
                "Query ContainerAppConsoleLogs_CL in Log Analytics for the last hour",
            ),
            B.explain(
                "ai-container-troubleshoot-x1",
                "Explain how you would find out why a container on AKS or Container Apps is not serving traffic.",
                "On AKS I start with kubectl get pods for the status, then kubectl describe pod, whose events show image pull errors, scheduling problems and probe failures. If the pod is in CrashLoopBackOff I read kubectl logs with the previous flag to see the crash. A pod that is running but not ready has a failing readiness probe. In Container Apps I check the revision status, then the system log stream for pulls and probes and the console log for my own output, and I confirm the ingress target port matches the app.",
                "Status and events first" to listOf("describe", "events", "status", "kubectl get"),
                "Logs of the crashed container" to listOf("previous", "crashloopbackoff", "kubectl logs", "console log"),
                "Probes, ingress and ports" to listOf("probe", "readiness", "ingress", "target port"),
            ),
            B.glossary(
                "CrashLoopBackOff" to "A container keeps exiting and Kubernetes keeps restarting it, waiting longer each time.",
                "ImagePullBackOff" to "The node cannot pull the image and is waiting before trying again.",
                "Liveness probe" to "A check that restarts a container that has stopped responding.",
                "Readiness probe" to "A check that keeps a container out of traffic until it is ready.",
                "System logs" to "Container Apps messages from the platform: pulls, probes, revisions.",
                "Console logs" to "Your container's stdout and stderr.",
                "Container insights" to "Azure Monitor's collection of AKS container logs and Kubernetes events into Log Analytics.",
            ),
            B.recap(
                "kubectl get for status, describe for events, logs --previous for crashes.",
                "Liveness restarts, readiness removes from traffic; a wrong path or port is the usual cause.",
                "Container Apps: revision and replica status, system and console logs, exec, ingress target port; Log Analytics keeps the history.",
            ),
        ),
    )

    val topics: List<Topic> = listOf(aks, troubleshoot)

    val quiz: Quiz = Quiz(
        id = "ai-containers-test", title = "Containers module test",
        questions = B.spread(listOf(
            B.q("ai-c0a", "ai-app", "Zaiqa must write an order, its order lines and the payment so that either all three land or none of them do. Which store?",
                listOf("Azure SQL Database", "Cosmos DB for NoSQL", "Azure Managed Redis", "Blob Storage"), setOf(0),
                "Several tables changing together is a transaction, which is what the relational source of truth is for.",
                whyWrong = mapOf(
                    1 to "Cosmos takes the huge, fast writes beside the order, not the money that has to add up across tables.",
                    2 to "Redis holds what is true right now and loses it on a restart; an order total is not a cache entry.",
                    3 to "Blob Storage holds files. An order is rows, not a file.",
                ),
            ),
            B.q("ai-c0b", "ai-app", "Which pair of Zaiqa facts belongs in Cosmos DB and in Redis, in that order?",
                listOf("The rider ping history, and the rider's current position", "The payments table, and the menu photos", "The rider's current position, and the rider ping history", "The order lines, and the finance report"), setOf(0),
                "Cosmos takes the huge history with its change feed; Redis takes the one value that is true right now.",
                whyWrong = mapOf(
                    1 to "Payments belong in Azure SQL and photos belong in Blob Storage; neither of those is in either store.",
                    2 to "That is the right idea the wrong way round: the history is the huge one, the position is the momentary one.",
                    3 to "Order lines and the report the finance team runs are both reasons Zaiqa keeps a relational database.",
                ),
            ),
            B.q("ai-c1", "ai-acr", "You want ACR to build the Coach API image from your Dockerfile without Docker installed on your laptop. Which command?",
                listOf("az acr build --registry ttcoach --image coach-api:v2 .", "docker build -t coach-api .", "az acr import --name ttcoach --source coach-api", "az acr login --name ttcoach"), setOf(0),
                "az acr build is an ACR Tasks quick task: it uploads the build context and builds and pushes the image in Azure.",
                whyWrong = mapOf(
                    1 to "docker build needs Docker running locally, which is what you wanted to avoid.",
                    2 to "Import copies an image that already exists in another registry; it does not build a Dockerfile.",
                    3 to "Login only authenticates your local Docker client to the registry.",
                ),
            ),
            B.q("ai-c2", "ai-acr", "The Coach image should rebuild automatically whenever its Python base image is updated. What do you use?",
                listOf("An ACR task with a base image update trigger", "A quick task run by hand each week", "Geo-replication", "A retention policy"), setOf(0),
                "ACR Tasks can trigger a build on a source commit or when the base image changes, keeping images patched.",
                whyWrong = mapOf(
                    1 to "A manual run each week is neither automatic nor prompt when a security fix lands.",
                    2 to "Geo-replication copies a registry to other regions; it builds nothing.",
                ),
            ),
            B.q("ai-c3", "ai-acr", "Why tag images coach-api:v2, coach-api:v3 rather than always pushing coach-api:latest?",
                listOf("Each deployment then points at a known, fixed image you can roll back to", "latest is not allowed in ACR", "Unique tags make images smaller", "ACR deletes latest after a day"), setOf(0),
                "Unique, stable tags make deployments repeatable and rollbacks meaningful.",
                whyWrong = mapOf(
                    1 to "latest is allowed; it is just a tag that moves, which is the problem.",
                    2 to "A tag is a name. It has no effect on the image size.",
                ),
            ),
            B.q("ai-c4", "ai-acr", "The Coach registry must be replicated to a second region close to users in Europe. Which SKU is required?",
                listOf("Premium", "Basic", "Standard"), setOf(0),
                "Geo-replication is a Premium feature of Azure Container Registry.",
                whyWrong = mapOf(
                    1 to "Basic is the entry tier with the least storage and no geo-replication.",
                    2 to "Standard adds storage and throughput but not geo-replication.",
                ),
            ),
            B.q("ai-c5", "ai-appservice-containers", "The Coach container on App Service listens on port 8000, and the site returns errors as the container never answers. What app setting fixes it?",
                listOf("WEBSITES_PORT=8000", "PORT_FORWARD=8000", "ASPNETCORE_URLS=8000", "WEBSITE_RUN_FROM_PACKAGE=1"), setOf(0),
                "For a custom container, WEBSITES_PORT tells App Service which port to send requests to.",
                whyWrong = mapOf(
                    1 to "That is not an App Service setting; the platform ignores it.",
                    2 to "That variable configures ASP.NET Core apps, not the Python container or the platform.",
                    3 to "Run from package is for code deployments from a zip file, not containers.",
                ),
            ),
            B.q("ai-c6", "ai-appservice-containers", "The Coach container on App Service needs the Cosmos DB key without it appearing in any app setting value. What do you use?",
                listOf("A Key Vault reference in the app setting, read with the app's managed identity", "The key typed into the Dockerfile as ENV", "A plain app setting with the key", "A file baked into the image"), setOf(0),
                "The setting holds only a reference; App Service fetches the secret with the managed identity and passes it as an environment variable.",
                whyWrong = mapOf(
                    1 to "An ENV line is baked into every image layer and visible to anyone who can pull it.",
                    2 to "Then the key itself sits in the setting, which is what you wanted to avoid.",
                ),
            ),
            B.q("ai-c7", "ai-appservice-containers", "How do App Service app settings reach a custom container?",
                listOf("As environment variables", "As a file at /app/settings.json", "As command-line arguments", "They do not; containers cannot read them"), setOf(0),
                "App settings are injected into the container as environment variables, so os.environ reads them.",
                whyWrong = mapOf(
                    1 to "App Service writes no such file; you would be reading something that does not exist.",
                    3 to "They do reach the container, as environment variables.",
                ),
            ),
            B.q("ai-c8", "ai-appservice-containers", "App Service should pull the Coach image from a private ACR without storing the registry admin password. What do you configure?",
                listOf("A managed identity with AcrPull, used for the image pull", "The admin user enabled on ACR", "A public registry", "A pull secret in an app setting"), setOf(0),
                "With a managed identity that holds AcrPull on the registry, App Service pulls the image with no stored password.",
                whyWrong = mapOf(
                    1 to "The admin user is a shared password, which is what you were told not to store.",
                    2 to "Making the registry public exposes the images to everyone.",
                ),
            ),
            B.q("ai-c9", "ai-container-apps", "You want 10 percent of Coach traffic on a new revision and 90 percent on the old one. What must be true?",
                listOf("The app is in multiple revision mode with traffic weights set", "The app is in single revision mode", "Two separate environments", "Two container apps behind a load balancer"), setOf(0),
                "Traffic splitting between revisions needs multiple revision mode; you then set a weight per revision.",
                whyWrong = mapOf(
                    1 to "Single revision mode keeps one active revision with all the traffic.",
                    2 to "Revisions of one app live in one environment; you do not need a second.",
                ),
            ),
            B.q("ai-c10", "ai-container-apps", "The Coach worker in Container Apps should scale from zero up with the number of messages in a Service Bus queue. What do you add?",
                listOf("A KEDA scale rule for the Service Bus queue, with min replicas 0", "An HTTP scale rule", "A fixed replica count of 10", "A liveness probe"), setOf(0),
                "Container Apps scales with KEDA rules; a Service Bus queue rule plus min replicas 0 gives event-driven scale to zero.",
                whyWrong = mapOf(
                    1 to "An HTTP rule scales on incoming requests, and a queue worker receives none.",
                    2 to "A fixed count never scales and never reaches zero.",
                    3 to "Probes check health; they do not decide how many replicas run.",
                ),
            ),
            B.q("ai-c11", "ai-container-apps", "How does a Container Apps environment variable read a value stored as a Container Apps secret?",
                listOf("Its value is secretref: followed by the secret name", "Its value is the secret's plain text", "Secrets cannot be used as environment variables", "Through a mounted Kubernetes Secret manifest"), setOf(0),
                "Define the secret on the app, then set the environment variable to secretref:<secret-name>.",
                whyWrong = mapOf(
                    1 to "Then the value sits in the app configuration, which a secret is meant to avoid.",
                    2 to "They can; secretref is exactly how.",
                    3 to "Container Apps hides Kubernetes. You never write Secret manifests there.",
                ),
            ),
            B.q("ai-c12", "ai-container-apps", "Which change to a container app creates a new revision?",
                listOf("Changing the container image tag", "Changing the traffic weights", "Adding a secret value only", "Renaming a tag on the app resource"), setOf(0),
                "Revision-scope changes, such as the image, containers or scale rules, create a new revision; application-scope changes do not.",
                whyWrong = mapOf(
                    1 to "Traffic weights are application scope; they move traffic between existing revisions.",
                    2 to "Secrets are application scope and do not create a revision on their own.",
                ),
            ),
            B.q("ai-c13", "ai-aks", "Which kubectl command deploys the objects in coach.yaml, and updates them if they already exist?",
                listOf("kubectl apply -f coach.yaml", "kubectl run coach.yaml", "kubectl logs -f coach.yaml", "kubectl expose coach.yaml"), setOf(0),
                "kubectl apply -f creates or updates every object described in the manifest file.",
                whyWrong = mapOf(
                    1 to "kubectl run starts a single pod from an image name; it does not read manifests.",
                    2 to "kubectl logs reads container output; -f there means follow.",
                ),
            ),
            B.q("ai-c14", "ai-aks", "The Coach Service exists but has no endpoints, though three pods are running and ready. What is the likely cause?",
                listOf("The Service selector does not match the pods' labels", "The Deployment has too many replicas", "The image is not in ACR", "The node pool is too small"), setOf(0),
                "A Service finds pods by label. If its selector matches none, it has no endpoints even with healthy pods.",
                whyWrong = mapOf(
                    1 to "The replica count does not stop a Service from selecting pods.",
                    2 to "The pods are running, so the image was pulled.",
                ),
            ),
            B.q("ai-c15", "ai-aks", "Pods on AKS must read Key Vault with an Entra identity and no stored secret. What do you set up?",
                listOf("Workload identity: a managed identity federated with a Kubernetes service account", "A Kubernetes Secret holding a client secret", "--attach-acr on the cluster", "A ConfigMap with the vault URL only"), setOf(0),
                "Workload identity federates a user-assigned managed identity with a service account; DefaultAzureCredential then picks it up.",
                whyWrong = mapOf(
                    1 to "A client secret in a Secret is still a stored credential.",
                    2 to "--attach-acr grants the nodes AcrPull for images; it gives your code no access to Key Vault.",
                    3 to "The URL says where the vault is, not who you are.",
                ),
            ),
            B.q("ai-c16", "ai-aks", "The team needs GPU node pools and direct access to Kubernetes objects for a custom operator. Which service fits?",
                listOf("Azure Kubernetes Service", "Azure Container Apps", "App Service for containers"), setOf(0),
                "AKS gives you the Kubernetes API and your own node pools, including GPU nodes.",
                whyWrong = mapOf(
                    1 to "Container Apps hides the Kubernetes API, so a custom operator has nothing to work with.",
                    2 to "App Service runs web containers; it offers no Kubernetes API or node pools.",
                ),
            ),
            B.q("ai-c17", "ai-container-troubleshoot", "A pod is in CrashLoopBackOff. Which command shows the error that made it crash?",
                listOf("kubectl logs <pod> --previous", "kubectl get service", "az acr repository list", "kubectl get nodes"), setOf(0),
                "--previous shows the output of the container instance that just exited, where the crash is recorded.",
                whyWrong = mapOf(
                    1 to "Services are about routing traffic; they say nothing about why a container exited.",
                    2 to "The image was pulled, so the registry is not the problem.",
                ),
            ),
            B.q("ai-c18", "ai-container-troubleshoot", "Which Log Analytics table holds the Coach container's own stdout in Container Apps?",
                listOf("ContainerAppConsoleLogs_CL", "ContainerAppSystemLogs_CL", "KubeEvents", "AzureActivity"), setOf(0),
                "Console logs are the app's stdout and stderr; system logs are the platform's messages.",
                whyWrong = mapOf(
                    1 to "System logs hold the platform's messages, such as pulls and probes, not your print statements.",
                    2 to "KubeEvents is an AKS table from Container insights.",
                    3 to "The activity log records control-plane operations on Azure resources, not app output.",
                ),
            ),
            B.q("ai-c19", "ai-container-troubleshoot", "AKS pods are Running with READY 0/1 and no restarts. What do you check?",
                listOf("The readiness probe's path and port", "The liveness probe's restart count", "Whether ACR geo-replication is on", "The Service type"), setOf(0),
                "A failing readiness probe keeps a running pod out of traffic without restarting it.",
                whyWrong = mapOf(
                    1 to "A failing liveness probe would restart the container, and these pods never restart.",
                    3 to "The Service type decides how traffic arrives, not whether a pod reports ready.",
                ),
            ),
            B.q("ai-c20", "ai-container-troubleshoot", "You want to run a command inside a running Coach replica in Container Apps to test a connection to Redis. What do you use?",
                listOf("az containerapp exec", "az containerapp logs show", "az containerapp revision list", "kubectl exec"), setOf(0),
                "az containerapp exec opens an interactive console in a running replica.",
                whyWrong = mapOf(
                    1 to "Logs only show output; you cannot run a command there.",
                    2 to "That lists revisions and their status, not a shell.",
                    3 to "Container Apps gives you no kubectl access to its underlying cluster.",
                ),
            ),
        )),
    )
}
