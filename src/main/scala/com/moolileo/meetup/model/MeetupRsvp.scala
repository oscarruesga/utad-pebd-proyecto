package com.moolileo.meetup.model

case class MeetupEvent(
    event_id: String,
    event_name: Option[String],
    event_url: Option[String],
    time: Option[Long]
                      )


case class MeetupGroupTopics(
                              topic_name: String,
                              urlkey: Option[String]
                            )

case class MeetupGroup(
                        group_id: Long,
                        group_name: String,
                        group_city: Option[String],
                        group_country: String,
                        group_state: Option[String],
                        group_urlname: Option[String],
                        group_lat: String,
                        group_lon: String,
                        group_topics: List[MeetupGroupTopics]
                      )

case class MeetupMember(
    member_id: Long,
    member_name: Option[String],
    other_services: Option[String],
    photo: Option[String]
                       )

case class MeetupVenue(
    venue_id: Option[Int],
    venue_name: Option[String],
    lat: Option[String],
    lon: Option[String]
                      )

case class MeetupRsvp(
    rsvp_id: Long,
    response: String,
    guests: Int,
    mtime: Long,
    visibility : String,
    event: MeetupEvent,
    group: MeetupGroup,
    member: MeetupMember,
    venue: MeetupVenue
                     )

case class TotalCountryAttendees (
                                   country: String,
                                   coordinates: String,
                                   attendees: Long
                                 )


case class CountryAttendeesByDate (
                                    key: String,
                                    country: String,
                                    coordinates: String,
                                    attendees: Long,
                                    day: Long,
                                    month: Long,
                                    year: Long
                                  )

case class GlobalTrendingTopics(
                                 topic: String,
                                 counter: Long,
                                 date: Long
                               )

case class CountryTrendingTopics(
                                  key: String,
                                  country: String,
                                  coordinates: String,
                                  topic: String,
                                  counter: Long,
                                  date: Long
                                )