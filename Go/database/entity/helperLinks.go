package entity

import (
	"log"
	"shortlinks-redirector/database"
	_ "shortlinks-redirector/database"

	_ "github.com/lib/pq"
)

func UpdateClicks(id int64) bool {

	queryUpdateClicks := `UPDATE "links" SET clicks = clicks + 1 WHERE id = $1`
	_, err := database.DB.Exec(queryUpdateClicks, id)
	if err != nil {
		log.Fatalf("[ERROR] cannot update clicks: %s\n", err)
		return false
	}

	return true
}
