package main

import (
	"database/sql"
	"fmt"
	"flag"
	"log"
	"net/http"

	"github.com/gin-gonic/gin"
	_"github.com/lib/pq"
)

func main() {
	connStr := "host=localhost port=5432 user=postgres password=root dbname=postgres sslmode=disable"

	db, err := sql.Open("postgres", connStr)
	if err != nil {
		log.Fatalf("[ERROR] Connect to bd: %v", err)
	}
	defer db.Close()

	if err = db.Ping(); err != nil {
		log.Fatalf("[ERROR] The database is unavailable: %v ", err)
	}
	fmt.Println("[SUCCESS] Connect to BD successfuly!");

	port := flag.String("port", "8081", "Port for HTTP-server")
	flag.Parse()

	r := gin.Default()

	r.GET("/:shortCode", func(c *gin.Context) {
			shortCode := c.Param("shortCode")
    
    	log.Printf("[DEBUG] Получен чистый shortCode из браузера: '%s'\n", shortCode)

    	var originalURL string

    	query := `SELECT original_link FROM "links" WHERE new_link = $1 LIMIT 1`
		
    	//log.Printf("[DEBUG] Выполняем SQL запрос: SELECT original_link FROM \"links\" WHERE new_link = '%s'\n", shortCode)
		
    	err := db.QueryRow(query, shortCode).Scan(&originalURL)

    	if err != nil {
        	//log.Fatalf("[DATABASE ERROR] Ошибка выполнения запроса в БД: %v\n", err)
			
        	c.String(http.StatusNotFound, fmt.Sprintf("[ERROR] Ссылка не найдена. Ошибка базы: %v", err))
        	return
    	}

    	log.Printf("[SUCCESS] Ссылка найдена! Перенаправляем на: %s\n", originalURL)
    	c.Redirect(http.StatusFound, originalURL)
	})

	fmt.Println("Redirector on Go started on port: %s...\n", *port)
	log.Fatal(r.Run(":" + *port))
}
