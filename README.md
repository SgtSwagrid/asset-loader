# scala-library-template

A reusable template for Scala libraries, with automatic [Maven Central](https://central.sonatype.com/) deployment.

## Using this template

### 1. Create your repository

Click '**Use this template**' on GitHub, and follow the instructions to create a new repository.

### 2. Update [build.sbt](build.sbt)

Replace every `example` placeholder with real values for your project.
In particular, make sure to at least update the following fields:

- `ThisBuild / name`: The name of your library.
- `ThisBuild / description`: A short description of your library.
- `ThisBuild / organization`: The namespace for your library, which should match your namespace on Maven Central (see step 3 below).
- `ThisBuild / oranizationName`: Your name or your organisation's name.
- `ThisBuild / organizationHomepage`: A URL for your organisation (e.g. your personal website).
- `ThisBuild / scmInfo`: The URL of your new GitHub repository.
- `ThisBuild / licenses`: The license under while your library is released.
  At the same time, update `LICENSE.md` to match.
- `ThisBuild / developers`: Details for the contributors to this project.
- Also change the name of the main subproject from `root` to the name of your library,
  as this name is taken during deployment.

### 3. Set up your Maven Central account

1. Create an account on [Maven Central](https://central.sonatype.com) to enable publishing, if you don't already have one.
2. [Register](https://central.sonatype.com/publishing/namespaces) your namespace (e.g. `org.yourname`).
   This should match the `organization` setting in [build.sbt](build.sbt).
3. [Generate](https://central.sonatype.com/usertoken) a user token.
   This will give you a username and password, which you can add as repository secrets (see below).

### 4. Generate a GPG key for signing releases

Run the following on your local machine to generate a GPG key for signing releases.

```bash
# Generate a new GPG key, making sure to note your passphrase:
gpg --gen-key

# Expose the secret key in base64:
gpg --armor --export-secret-keys <PUBLIC_KEY> | base64

# Upload the public key to a keyserver:
gpg --keyserver keyserver.ubuntu.com --send-keys <PUBLIC_KEY>
```

### 5. Add repository secrets

Add the following secrets to your repository on GitHub
(**Settings → Secrets and variables → Actions**):

| Secret                    | Value                                                                                                                                        |
|---------------------------|----------------------------------------------------------------------------------------------------------------------------------------------|
| `SONATYPE_USERNAME`       | Username from Maven user token in step 3.                                                                                                    |
| `SONATYPE_PASSWORD`       | Password from Maven user token in step 3.                                                                                                    |
| `PGP_SECRET`              | Base64-encoded GPG private key from step 4.                                                                                                  |
| `PGP_PASSPHRASE`          | Passphrase used when generating the GPG key in step 4.                                                                                       |
| `GH_TOKEN`                | Your GitHub [PAT](https://github.com/settings/personal-access-tokens) with administrator to access your repository.                          |
| `CLAUDE_CODE_OAUTH_TOKEN` | API key from [Claude](https://claude.com/product/claude-code) for agentic workflows (optional). |


### 6. Publishing workflow

Releases are automatically published to Maven Central when you create a new release on GitHub.

#### Example:

To release version `1.2.3`, go to **Releases → Draft a new release** on GitHub, create a tag `v1.2.3`, and click **Publish release**.

## See also

Check out [scala-website-template](https://github.com/SgtSwagrid/scala-website-template) for a similar template to quickly start a full stack website in Scala.
