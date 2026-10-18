package contracts.user

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    description "Deve retornar 404 ao excluir um usuario inexistente"
    request {
        method DELETE()
        url("/api/v1/users/5f8e2a1c-9b3d-4e7f-8a6c-1d2e3f4a5b6c")
    }
    response {
        status 404
        headers {
            contentType "application/problem+json"
        }
        body([
                status: 404,
                title: "User not found",
                type: "/errors/user-not-found",
                detail: "User not found with id: 5f8e2a1c-9b3d-4e7f-8a6c-1d2e3f4a5b6c"
        ])
    }
}

