package contracts.user

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    description "Deve anonimizar um usuario (204)"
    request {
        method DELETE()
        url("/api/v1/users/0c9b6f3e-5a2d-4b8e-9f1a-7d3c2e1b4a50")
    }
    response {
        status 204
    }
}

