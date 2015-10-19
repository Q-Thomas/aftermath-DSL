String repo = 'tass-belgium/picotcp'
String slave = 'normal'
String job1_descr = 'Perform a PicoTCP build'
String job2_descr = 'Run unit tests on PicoTCP'

URL branchUrl = "https://api.github.com/repos/$repo/branches".toURL()
List branches = new JsonSlurper().parse(branchUrl.newReader())
branches.each { branch ->

    String safeBranchName = branch.name.replaceAll('/', '-')

    folder "$basePath/$safeBranchName"

    job("$basePath/$safeBranchName/pico-build") {
        description(job1_descr)
        label(slave)
        scm {
            github repo, branch.name
        }
        triggers {
            scm 'H/5 * * * *'
        }
        steps {
            shell 'make clean'
            shell 'make'

    }

    job("$basePath/$safeBranchName/pico-units") {
        description(job2_descr)
        label(slave)
        scm {
            github repo, branch.name
        }
        steps {
            shell 'make clean'
            shell 'make units ARCH=faulty'
        }
    }
}