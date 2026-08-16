package links

import (
	"log"
	"shortLinks/database"

	_ "github.com/lib/pq"
)

func UpdateClicks(id int64) bool {

	queryUpdateClicks := `UPDATE "links" SET clicks = clicks + 1 WHERE id = $1`
	_, err := database.DB.Exec(queryUpdateClicks, id)
	if err != nil {
		log.Printf("[ERROR][UPDATE_CLICKS] cannot update clicks: %s\n", err)
		return false
	}

	return true

}

func IsActiveLink(id int64) (bool, error) {

	var isActive bool

	query := `SELECT is_active from "links" where id = $1 LIMIT 1`
	err := database.DB.QueryRow(query, id).Scan(&isActive)

	if err != nil {
		log.Printf("[ERROR][GET_IS_ACTIVE_LINK] cannot get active " +
							"link status: %s\n", err)
		return false, err
	}

	return isActive, nil

}
