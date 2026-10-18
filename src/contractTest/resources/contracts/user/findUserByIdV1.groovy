package contracts.user

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    description "Deve consultar um usuario por ID"
    request {
        method GET()
        headers {
            accept "application/json"
        }
        url("/api/v1/users/0c9b6f3e-5a2d-4b8e-9f1a-7d3c2e1b4a50")
    }
    response {
        status 200
        headers {
            contentType "application/json"
        }
        body([
                id: "0c9b6f3e-5a2d-4b8e-9f1a-7d3c2e1b4a50",
                name: "Joao Silva",
                email: "joao@algashop.com",
                type: "CUSTOMER",
                enabled: true
        ])
    }
}

