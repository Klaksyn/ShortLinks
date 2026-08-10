package database

import (
	"database/sql"
	"fmt"
	"log"
	_ "net/http"

	_ "github.com/lib/pq"
)

var DB *sql.DB

func Connect() {
	connStr := "host=localhost port=5432 user=postgres password=root dbname=postgres sslmode=disable"

	db, err := sql.Open("postgres", connStr)
	if err != nil {
		log.Fatalf("[ERROR] Connect to bd: %v", err)
	}

	if err = db.Ping(); err != nil {
		log.Fatalf("[ERROR] The database is unavailable: %v ", err)
	}
	fmt.Println("[SUCCESS] Connect to BD successfuly!")
}
