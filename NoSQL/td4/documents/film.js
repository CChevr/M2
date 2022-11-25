db.dbma.drop()

db.dbma.insert(
    [
        {   
            "idFilm" : 1,
            "movie" : "A History of Violence", 
            "year" : 2005,
            "summary" : "Tom Stall, a humble family man and owner of a popular neighborhood restaurant, lives a quiet but fulfilling existence in the Midwest. One night Tom foils a crime at his place of business and, to his chagrin, is plastered all over the news for his heroics. Following this, mysterious people follow the Stalls' every move, concerning Tom more than anyone else. As this situation is confronted, more lurks out over where all these occurrences have stemmed from compromising his marriage, family relationship and the main characters' former relations in the process.",
            "country" : "USA",
            "genre" : {
                "idGenre" : 1,
                "label" : "Crime"
            },
            "director" : {
                "idPerson" : 1,
                "firstname" : "David",
                "lastname" : "Cronenberg",
                "birth" : 1943
            },
            "actors" : [
                {   
                    "person" : {
                        "idPerson" : 2,
                        "firstname" : "Ed",
                        "lastname" : "Harris",
                        "birth" : 1950
                    },
                    "role" : "Carl Fogarty"
                },
                {   
                    "person" : {
                        "idPerson" : 3,
                        "firstname" : "Vigo",
                        "lastname" : "Mortensen",
                        "birth" : 1958
                    },
                    "role" : "Tom Stall"
                },
                {   
                    "person" : {
                        "idPerson" : 4,
                        "firstname" : "Maria",
                        "lastname" : "Bello",
                        "birth" : 1943
                    },
                    "role" : "Richie Cusack"
                }

            ]
        },
        {   
            "idFilm" : 2,
            "movie" : "The Social network", 
            "year" : 2010,
            "summary" : "On a fall night in 2003, Harvard undergrad and computer programming genius Mark Zuckerberg sits down at his computer and heatedly begins working on a new idea. In a fury of blogging and programming, what begins in his dorm room soon becomes a global social network and a revolution in communication. A mere six years and 500 million friends later, Mark Zuckerberg is the youngest billionaire in history... but for this entrepreneur, success leads to both personal and legal complications",
            "country" : "USA",
            "genre" : {
                "idGenre" : 2,
                "label" : "Drama"
            },
            "director" : {
                "idPerson" : 7,
                "firstname" : "David",
                "lastname" : "Fincher",
                "birth" : 1962
            },
            "actors" : [
                {   
                    "person" : {
                        "idPerson" : 5,
                        "firstname" : "Jesse",
                        "lastname" : "Eisenber",
                        "birth" : 1983
                    },
                    "role" : "Mark Zuckerberg"
                },
                {   
                    "person" : {
                        "idPerson" : 6,
                        "firstname" : "Rooney",
                        "lastname" : "Mara",
                        "birth" : 1985
                    },
                    "role" : "Erica Albrig"
                }
            ]
        }
    ]
)