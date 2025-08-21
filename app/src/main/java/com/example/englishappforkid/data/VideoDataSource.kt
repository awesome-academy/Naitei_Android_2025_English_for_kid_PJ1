package com.example.englishappforkid.data

import com.example.englishappforkid.data.model.VideoItem
import java.util.UUID
import kotlin.collections.find

object VideoDataSource {
    val videoStories: List<VideoItem> =
        listOf(
            VideoItem(
                id = UUID.randomUUID().toString(),
                title = "Goldilocks and the three bears",
                videoId =
                    "https://firebasestorage.googleapis.com/v0/b/englishappforkid.firebasestorage.app" +
                            "/o/Oaw-d3r_gIc.mp4?alt=media&token=dc98f141-ab59-4530-a4fb-0c405b19f65f",
                thumbnailUrl = "https://i.ytimg.com/vi/GbzMC6qAzVU/hqdefault.jpg",
                description =
                    "The three bears eat hot cereal called 'porridge' for breakfast. " +
                            "What do you usually have for breakfast? Tell us about it!",
            ),
            VideoItem(
                id = UUID.randomUUID().toString(),
                title = "Jack and the beanstalk",
                videoId =
                    "https://firebasestorage.googleapis.com/v0/b/englishappforkid." +
                            "firebasestorage.app/o/rKB1_wBueFM.mp4?alt=media&token=d1562a98-da9e-450c-9fd1-a9eb5e2c2d98",
                thumbnailUrl = "https://i.ytimg.com/vi/rKB1_wBueFM/hqdefault.jpg",
                description =
                    "The giant in this story has a magic harp that can sing and a magic hen " +
                            "that lays golden eggs! Imagine that some of the things in your house are magic. " +
                            "Tell us about them!",
            ),
            VideoItem(
                id = UUID.randomUUID().toString(),
                title = "Little Red Riding Hood",
                videoId =
                    "https://firebasestorage.googleapis.com/v0/b/" +
                            "englishappforkid.firebasestorage.app/o/GbzMC6qAzVU.mp4?alt=media&token=" +
                            "e768c61e-4565-4136-b189-a36ab409c760",
                thumbnailUrl = "https://i.ytimg.com/vi/Un4NWwcObRA/hqdefault.jpg",
                description =
                    "Little Red Riding Hood goes to visit her granny. Do you go to visit " +
                            "your grandparents or other members of your family? Tell us about them!",
            ),
            VideoItem(
                id = UUID.randomUUID().toString(),
                title = "Ali and the magic carpet",
                videoId =
                    "https://firebasestorage.googleapis.com/v0/b/englishappforkid." +
                            "firebasestorage.app/o/mQ0MWFe4gs0.mp4?alt=media&token=d48e0041-60b7" +
                            "-4bc3-96ee-879ad2cec089",
                thumbnailUrl = "https://i.ytimg.com/vi/Db9vLf1MuPg/hqdefault.jpg",
                description =
                    "Imagine you find a magic carpet and it will fly anywhere you want to go! " +
                            "Where do you go? What do you see when you get there? What's the weather like? " +
                            "Tell us about it!",
            ),
            VideoItem(
                id = UUID.randomUUID().toString(),
                title = "Record breakers",
                videoId =
                    "https://firebasestorage.googleapis.com/v0/b/englishappforkid." +
                            "firebasestorage.app/o/G9B3ZGZ0nw0.mp4?alt=media&token=" +
                            "f06661af-5f2c-48dd-8e8f-cb63469c72e8",
                thumbnailUrl = "https://i.ytimg.com/vi/lxfaVWsHQEM/hqdefault.jpg",
                description =
                    "There are lots of different world records in this story. " +
                            "Which record would you like to try and break? Tell us about it!",
            ),
            VideoItem(
                id = UUID.randomUUID().toString(),
                title = "The greedy hippo",
                videoId =
                    "https://firebasestorage.googleapis.com/v0/b/englishappforkid." +
                            "firebasestorage.app/o/dbLecFXK00I.mp4?alt=media&token=" +
                            "abca3f91-ce66-46dd-a9ce-23913b00e2fa",
                thumbnailUrl = "https://i.ytimg.com/vi/4x7ewDWR6NQ/hqdefault.jpg",
                description =
                    "The animals make a horrible pie for the hippo with fish and soap! " +
                            "Can you invent another horrible meal for the greedy hippo? Tell us about it!",
            ),
            VideoItem(
                id = UUID.randomUUID().toString(),
                title = "My secret team",
                videoId =
                    "https://firebasestorage.googleapis.com/v0/b/englishappforkid." +
                            "firebasestorage.app/o/Un4NWwcObRA.mp4?alt=media&token=050aceb6-" +
                            "7737-423c-94b8-8f4682b4bc5a",
                thumbnailUrl =
                    "https://i.ytimg.com/vi/Un4NWwcObRA/maxresdefault.jpg?sqp=" +
                            "-oaymwEmCIAKENAF8quKqQMa8AEB-AH-CYAC0AWKAgwIABABGFsgYihlMA8=&rs=AOn4" +
                            "CLB53Pe2hJjYH-FKVUxEHkEaGF66eQ",
                description =
                    "The boy in the story loves his favourite football team. " +
                            "What's your favourite sports team? Tell us about it!",
            ),
            VideoItem(
                id = UUID.randomUUID().toString(),
                title = "Superhero high",
                videoId =
                    "https://firebasestorage.googleapis.com/v0/b/englishappforkid." +
                            "firebasestorage.app/o/Db9vLf1MuPg.mp4?alt=media&token=0399c008-" +
                            "5169-40ac-a9d7-28dcfa1618cf",
                thumbnailUrl = "https://i.ytimg.com/vi/Db9vLf1MuPg/maxresdefault.jpg",
                description =
                    "These superheroes have some very exciting lessons! Imagine you're a " +
                            "student at Superhero High. Tell us about your day!",
            ),
            VideoItem(
                id = UUID.randomUUID().toString(),
                title = "The twins week",
                videoId =
                    "https://firebasestorage.googleapis.com/v0/b/englishappforkid." +
                            "firebasestorage.app/o/aU0OqbP4JD8.mp4?alt=media&token=b843352e-" +
                            "e39a-4cc2-8197-ed7cab58aa7c",
                thumbnailUrl =
                    "https://i.ytimg.com/vi/aU0OqbP4JD8/maxresdefault.jpg?sqp" +
                            "=-oaymwEmCIAKENAF8quKqQMa8AEB-AH-CYAC0AWKAgwIABABGE4gZSgyMA8=&rs=AOn4CLBy" +
                            "Px32gx9RiwqLfZsiYvWPdOlG-A",
                description =
                    "The twins do lots of different things with the animals at the zoo. " +
                            "What do you do on the different days of the week? Tell us about it!",
            ),
            VideoItem(
                id = UUID.randomUUID().toString(),
                title = "Dark, dark wood",
                videoId =
                    "https://firebasestorage.googleapis.com/v0/b/englishappforkid." +
                            "firebasestorage.app/o/4x7ewDWR6NQ.mp4?alt=media&token=a64b02c4-" +
                            "ef4c-4611-905c-287e48a283ed",
                thumbnailUrl =
                    "https://i.ytimg.com/vi/4x7ewDWR6NQ/maxresdefault.jpg?sqp" +
                            "=-oaymwEmCIAKENAF8quKqQMa8AEB-AH-CYAC0AWKAgwIABABGEIgXShyMA8=&rs=AOn4C" +
                            "LDcRxJ8vMbLKFQgKbht6o5Zg2ymaQ",
                description =
                    "Imagine you find another house in the dark, dark wood. What’s inside? " +
                            "Tell us about it!",
            ),
            VideoItem(
                id = UUID.randomUUID().toString(),
                title = "No dogs!",
                videoId =
                    "https://firebasestorage.googleapis.com/v0/b/englishappforkid." +
                            "firebasestorage.app/o/a3P5bX8gGYI.mp4?alt=media&token=aac2ed2b-" +
                            "5ab9-458c-bf0a-c5d43b62e0e5",
                thumbnailUrl =
                    "https://i.ytimg.com/vi/a3P5bX8gGYI/maxresdefault.jpg?sq" +
                            "p=-oaymwEmCIAKENAF8quKqQMa8AEB-AH-CYAC0AWKAgwIABABGF8gPihyMA8=&rs=A" +
                            "On4CLCZQmYickj6-sAv8CZL5IgCFqu6nw",
                description =
                    "At Katie's playground there are swings, a slide, a see-saw, a " +
                            "roundabout and a springy. Can you imagine the best playground in the world? " +
                            "Tell us about it",
            ),
            VideoItem(
                id = UUID.randomUUID().toString(),
                title = "Princess and the dragon",
                videoId =
                    "https://firebasestorage.googleapis.com/v0/b/englishappforkid." +
                            "firebasestorage.app/o/oepRTA4s4XM.mp4?alt=media&token=67c7dad8-" +
                            "c0d5-41cb-9603-b9796ee20799",
                thumbnailUrl =
                    "https://i.ytimg.com/vi/oepRTA4s4XM/maxresdefault.jpg?sq" +
                            "p=-oaymwEmCIAKENAF8quKqQMa8AEB-AH-CYAC0AWKAgwIABABGEQgRihyMA" +
                            "8=&rs=AOn4CLD6lfvV0GoG_9qHHdAqDB2n4AGWVA",
                description =
                    "There are lots of princes and princesses in storybooks and films. " +
                            "Who is your favourite prince or princess? Tell us about them!",
            ),
            VideoItem(
                id = UUID.randomUUID().toString(),
                title = "Why Anansi has thin legs",
                thumbnailUrl =
                    "https://i.ytimg.com/vi/D25eKPWa-j0/maxresdefault." +
                            "jpg?sqp=-oaymwEmCIAKENAF8quKqQMa8AEB-AH-CYAC0AWKAgwIABABGE" +
                            "8gXyhlMA8=&rs=AOn4CLD3jRY7OlGtLQtb-PhZcC-4m4SPlA",
                videoId =
                    "https://firebasestorage.googleapis.com/v0/b/englishappforkid." +
                            "firebasestorage.app/o/D25eKPWa-j0.mp4?alt=media&token=6a6614d" +
                            "f-33e2-4d54-adc5-e1101398ec64",
                description =
                    "Anansi is a very special spider. Do you like spiders or do you think " +
                            "they’re scary? Tell us what you think!",
            ),
            VideoItem(
                id = UUID.randomUUID().toString(),
                title = "Our colourful world",
                thumbnailUrl =
                    "https://i.ytimg.com/vi/lxfaVWsHQEM/maxresdefault.j" +
                            "pg?sqp=-oaymwEmCIAKENAF8quKqQMa8AEB-AH-CYAC0AWKAgwIABAB" +
                            "GEYgZSglMA8=&rs=AOn4CLApziZhP9x5qW7uhQzFLqKjpYVwSQ",
                videoId =
                    "https://firebasestorage.googleapis.com/v0/b/englishappforkid." +
                            "firebasestorage.app/o/lxfaVWsHQEM.mp4?alt=media&token=c6e7d1" +
                            "ba-3331-4d2a-9706-4d7085d199cd",
                description =
                    "In the story Billy and Splodge visit a green planet, a blue planet, " +
                            "a red planet, a pink planet and a yellow planet. Can you imagine another planet " +
                            "for Billy and Splodge to visit? Tell us about it!",
            ),
            VideoItem(
                id = UUID.randomUUID().toString(),
                title = "The monster shopping trip",
                videoId =
                    "https://firebasestorage.googleapis.com/v0/b/englishappforkid." +
                            "firebasestorage.app/o/BpGrac0Vm7M.mp4?alt=media&token=07fd522" +
                            "7-eb7f-498a-bf8f-7d5a63299145",
                thumbnailUrl =
                    "https://i.ytimg.com/vi/BpGrac0Vm7M/maxresdefault.jpg" +
                            "?sqp=-oaymwEmCIAKENAF8quKqQMa8AEB-AH-CYAC0AWKAgwIABABGE0gWShl" +
                            "MA8=&rs=AOn4CLBeYsrnEyrMFleKgUhoNH2aat0MYw",
                description =
                    "Hairy Henry's friends give him some really special birthday presents. " +
                            "What was your most special birthday present? Tell us about it!",
            ),
        )

    fun getVideoById(id: String): VideoItem? = videoStories.find { it.id == id }
}
