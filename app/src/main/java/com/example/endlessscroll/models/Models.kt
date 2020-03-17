package com.example.endlessscroll.models

data class EventList (var data: MutableList<Event>,
                      var links: Link,
                      var meta: Meta)
data class Event (var id: Int? = null,
                  var category_id: Int? = null,
                  var category: EventCategory? = null,
                  var gender_id: Int? = null,
                  var title: String? = null,
                  var description: String? = null,
                  var longitude: String? = null,
                  var latitude: String? = null,
                  var start_at: String? = null,
                  var is_moderated: Boolean? = null)
data class EventCategory (var id: Int? = null,
                          var name: String? = null,
                          var created_at: String? = null,
                          var updated_at: String? = null)
data class Link (var first: String?,
                 var last: String?,
                 var prev: String? = null,
                 var next: String? = null)
data class Meta (var current_page: Int?,
                 var from: Int?,
                 var last_page: Int?,
                 var path: String?,
                 var to: Int?,
                 var total: Int?)