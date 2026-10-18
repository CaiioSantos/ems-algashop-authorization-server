package contracts.user

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    description "Deve listar usuarios com paginacao"
    request {
        method GET()
        headers {
            accept "application/json"
        }
        url("/api/v1/users") {
            queryParameters {
                parameter('page', 0)
                parameter('size', 2)
            }
        }
    }
    response {
        status 200
        headers {
            contentType "application/json"
        }
        body([
                number: 0,
                size: 2,
                totalPages: 1,
                totalElements: 2,
                content: [
                        [
                                id: "0c9b6f3e-5a2d-4b8e-9f1a-7d3c2e1b4a50",
                                name: "Joao Silva",
                                email: "joao@algashop.com",
                                type: "CUSTOMER",
                                enabled: true
                        ],
                        [
                                id: "7a1b2c3d-4e5f-4a6b-8c7d-9e0f1a2b3c4d",
                                name: "Maria Souza",
                                email: "maria@algashop.com",
                                type: "OPERATOR",
                                enabled: true
                        ]
                ]
        ])
    }
}

