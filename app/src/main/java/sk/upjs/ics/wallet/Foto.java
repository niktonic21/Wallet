package sk.upjs.ics.wallet;

/**
 * Created by Maťo21 on 13. 6. 2015.
 */
public class Foto {

        private String uri;

        private String description;

        private String year;

        private String month;

        private String day;

        private String id;

        public String getUri() {
            return uri;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public void setUri(String uri) {
            this.uri = uri;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getYear() {
            return year;
        }

        public void setYear(String year) {
            this.year = year;
        }

        public String getMonth() {
            return month;
        }

        public void setMonth(String month) {
            this.month = month;
        }

        public String getDay() {
            return day;
        }

        public void setDay(String day) {
            this.day = day;
        }
}
