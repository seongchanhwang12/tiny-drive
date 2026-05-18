```mermaid
erDiagram
USERS {
BIGINT user_id PK
VARCHAR name
VARCHAR email UK
VARCHAR password_hash
DATETIME created_at
DATETIME updated_at
}

    ITEMS {
        BIGINT item_id PK
        BIGINT owner_id FK
        BIGINT parent_id FK "nullable, root이면 null"
        VARCHAR name
        VARCHAR type "FILE | FOLDER"
        VARCHAR extension "nullable"
        VARCHAR mime_type "nullable"
        BIGINT size "nullable"
        BOOLEAN trashed
        DATETIME created_at
        DATETIME updated_at
    }

    FILE_OBJECTS {
        BIGINT file_object_id PK
        BIGINT item_id FK
        VARCHAR storage_key
        VARCHAR checksum
        BIGINT size
        DATETIME created_at
    }

    DOWNLOAD_URLS {
        BIGINT download_url_id PK
        BIGINT item_id FK
        VARCHAR download_url
        INT expires_in
        DATETIME issued_at
        DATETIME expires_at
    }

    PERMISSIONS {
        BIGINT permission_id PK
        BIGINT item_id FK
        BIGINT user_id FK
        VARCHAR role "OWNER | READER | WRITER"
        DATETIME created_at
        DATETIME updated_at
    }

    USERS ||--o{ ITEMS : owns
    ITEMS ||--o{ ITEMS : contains
    ITEMS ||--o| FILE_OBJECTS : has_binary
    ITEMS ||--o{ DOWNLOAD_URLS : issues
    USERS ||--o{ PERMISSIONS : granted
    ITEMS ||--o{ PERMISSIONS : shared
 ```