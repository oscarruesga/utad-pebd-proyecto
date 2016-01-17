#!/bin/bash

set -x

HOST="http://localhost:9200"
SHARDS=3
REPLICAS=2

curl -XDELETE "$HOST/meetup-rsvps"

curl -XPUT "$HOST/meetup-rsvps/" -d "{
    \"settings\" : {
        \"index\" : {
            \"number_of_shards\" : $SHARDS,
            \"number_of_replicas\" : $REPLICAS
        }
    }
}"


curl -XPUT "$HOST/meetup-rsvps/_mappings/meetup-rsvps" -d '{
	"properties" : {
	    "event" : {
	      "properties" : {
	        "event_id" : {
	          "type" : "string"
	        },
	        "event_name" : {
	          "type" : "string"
	        },
	        "event_url" : {
	          "type" : "string",
	          "index": "not_analyzed"
	        },
	        "time" : {
	          "type": "date",
	          "doc_values": true,
	          "format": "epoch_millis"
	        }
	      }
	    },
	    "group" : {
	      "properties" : {
	        "group_city" : {
	          "type" : "string",
	          "index": "not_analyzed"
	        },
	        "group_country" : {
	          "type" : "string",
	          "index": "not_analyzed"
	        },
	        "group_id" : {
	          "type" : "long"
	        },
	        "group_lat" : {
	          "type" : "string"
	        },
	        "group_lon" : {
	          "type" : "string"
	        },
	        "group_name" : {
	          "type" : "string"
	        },
	        "group_state" : {
	          "type" : "string",
	          "index": "not_analyzed"
	        },
	        "group_topics" : {
	          "properties" : {
	            "topic_name" : {
	              "type" : "string",
	              "index": "not_analyzed"
	            },
	            "urlkey" : {
	              "type" : "string",
	              "index": "not_analyzed"
	            }
	          }
	        },
	        "group_urlname" : {
	          "type" : "string"
	        }
	      }
	    },
	    "guests" : {
	      "type" : "long"
	    },
	    "member" : {
	      "properties" : {
	        "member_id" : {
	          "type" : "long"
	        },
	        "member_name" : {
	          "type" : "string"
	        },
	        "photo" : {
	          "type" : "string"
	        }
	      }
	    },
	    "mtime" : {
	      "type": "date",
	      "doc_values": true,
	      "format": "epoch_millis"
	    },
	    "response" : {
	      "type" : "string",
	      "index": "not_analyzed"
	    },
	    "rsvp_id" : {
	      "type" : "long"
	    },
	    "venue" : {
	      "properties" : {
	        "lat" : {
	          "type" : "string"
	        },
	        "lon" : {
	          "type" : "string"
	        },
		"location" : {
	          "type" : "geo_point"
	        },
	        "venue_id" : {
	          "type" : "long"
	        },
	        "venue_name" : {
	          "type" : "string"
	        }
	      }
	    },
	    "visibility" : {
	      "type" : "string",
	      "index": "no"
	    }
	  }
	}'



curl -XDELETE "$HOST/meetup_total_attending"

curl -XPUT "$HOST/meetup_total_attending/" -d "{
    \"settings\" : {
        \"index\" : {
            \"number_of_shards\" : $SHARDS,
            \"number_of_replicas\" : $REPLICAS
        }
    }
}"


curl -XPUT "$HOST/meetup_total_attending/_mappings/meetup_total_attending" -d '{
      "properties": {
        "attendees": {
          "type": "long"
        },
        "country": {
          "type": "string"
        },
        "coordinates" : {
          "type" : "geo_point"
        }
      }
    }'
    
curl -XDELETE "$HOST/meetup_attending_by_date"

curl -XPUT "$HOST/meetup_attending_by_date/" -d "{
    \"settings\" : {
        \"index\" : {
            \"number_of_shards\" : $SHARDS,
            \"number_of_replicas\" : $REPLICAS
        }
    }
}"


curl -XPUT "$HOST/meetup_attending_by_date/_mappings/meetup_attending_by_date" -d '{
  "_timestamp": {
    "enabled": true
  },
  "properties": {
    "attendees": {
      "type": "long"
    },
    "country": {
      "type": "string"
    },
    "coordinates" : {
      "type" : "geo_point"
    },
    "day": {
      "type": "date",
      "doc_values": true,
      "format": "epoch_millis"
    },
    "month": {
      "type": "date",
      "doc_values": true,
      "format": "epoch_millis"
    },
    "year": {
      "type": "date",
      "doc_values": true,
      "format": "epoch_millis"
    },
    "key": {
      "type": "string"
    }
  }
}'

curl -XDELETE "$HOST/meetup_global_trending_topics"

curl -XPUT "$HOST/meetup_global_trending_topics/" -d "{
    \"settings\" : {
        \"index\" : {
            \"number_of_shards\" : $SHARDS,
            \"number_of_replicas\" : $REPLICAS
        }
    }
}"


curl -XPUT "$HOST/meetup_global_trending_topics/_mappings/meetup_global_trending_topics" -d '{
	  "_timestamp": {
	    "enabled": true
	  },
	  "properties": {
	    "topic": {
      	      "type": "string"
      	    },
	    "counter": {
	      "type": "long"
	    },
	    "date": {
	      "type": "date",
	      "doc_values": true,
	      "format": "epoch_millis"
	    }
	  }
	}'
	
curl -XDELETE "$HOST/meetup_country_trending_topics"

curl -XPUT "$HOST/meetup_country_trending_topics/" -d "{
    \"settings\" : {
        \"index\" : {
            \"number_of_shards\" : $SHARDS,
            \"number_of_replicas\" : $REPLICAS
        }
    }
}"


curl -XPUT "$HOST/meetup_country_trending_topics/_mappings/meetup_country_trending_topics" -d '{
	  "_timestamp": { 
	    "enabled": true
	  },
	  "properties": {
	    "counter": {
	      "type": "long"
	    },
	    "country": {
	      "type": "string"
	    },
	    "topic": {
	      "type": "string"
	    },
	    "coordinates" : {
          "type" : "geo_point"
        },
	    "key": {
	      "type": "string"
	    },
	    "date": {
	      "type": "date",
	      "doc_values": true,
	      "format": "epoch_millis"
	    }
	  }
	}'
