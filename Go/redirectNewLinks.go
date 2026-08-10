package main

import (
	"database/sql"
	_ "database/sql"
	"flag"
	"fmt"
	"log"
	"net/http"

	"shortlinks-redirector/database"
	"shortlinks-redirector/database/entity"

	"github.com/gin-gonic/gin"
)

func main() {
	database.Connect()
	defer func(Db *sql.DB) {
		err := Db.Close()
		if err != nil {
			log.Fatalf("[ERROR] The database is unavailable: %v ", err)
		}
	}(database.DB)

	port := flag.String("port", "8081", "Port for HTTP-server")
	flag.Parse()

	r := gin.Default()

	r.GET("/:shortCode", func(c *gin.Context) {
		shortCode := c.Param("shortCode")

		log.Printf("[DEBUG] Получен чистый shortCode из браузера: '%s'\n",
			shortCode)

		var originalURL string
		var linkId int64

		query := `SELECT id, original_link FROM "links" WHERE new_link = $1 LIMIT 1`
		log.Printf("[DEBUG] Выполняем SQL запрос: SELECT original_link FROM \"links\" WHERE new_link = '%s'\n", shortCode)

		err := database.DB.QueryRow(query, shortCode).Scan(&linkId, &originalURL)

		if err != nil {
			log.Printf("[DATABASE INFO] Ошибка выполнения запроса в БД: %v\n", err)

			c.String(http.StatusNotFound,
				fmt.Sprintf("[ERROR] Ссылка не найдена. Ошибка базы: %v", err))
			return
		}

		if !entity.UpdateClicks(linkId) {
			c.String(http.StatusInternalServerError,
				"[ERROR] ошибка при обновлении счетчика кликов")
			return
		}

		log.Printf("[SUCCESS] Ссылка найдена! Перенаправляем на: %s\n",
			originalURL)
		c.Redirect(http.StatusFound, originalURL)
	})

	fmt.Printf("Redirector on Go started on port: %s...\n", *port)
	log.Fatal(r.Run(":" + *port))
}
