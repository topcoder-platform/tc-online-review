## The payloads to kafka

Every payload has the same format, the difference just in `type` and `data` field. The basic payload is:

```json
{
  "topic": "or.notification.create",
  "originator": "tc-online-review",
  "mime-type": "application/json",
  "timestamp": "2022-05-31T06:15:05.584Z",
  "payload": {
    "challengeId": 721,
    "userId": 132456,
    "type": "messageType",
    "data": {}
  }
}
```

### Project status updated message

```json
{
  "topic": "or.notification.create",
  "originator": "tc-online-review",
  "mime-type": "application/json",
  "timestamp": "2022-05-31T06:15:05.584Z",
  "payload": {
    "challengeId": 721,
    "userId": 132456,
    "type": "status",
    "data": {
      "id": 1,
      "name": "Active",
      "description": ""
    }
  }
}
```

### Project category updated message

```json
{
  "topic": "or.notification.create",
  "originator": "tc-online-review",
  "mime-type": "application/json",
  "timestamp": "2022-05-31T06:15:05.584Z",
  "payload": {
    "challengeId": 721,
    "userId": 132456,
    "type": "category",
    "data": {
      "id": 34,
      "name": "other",
      "description": "",
      "projectType": {
        "id": 3,
        "name": "Studio",
        "description": "Studio"
      }
    }
  }
}
```

### Project prize updated message

```json
{
  "topic": "or.notification.create",
  "originator": "tc-online-review",
  "mime-type": "application/json",
  "timestamp": "2022-05-31T06:15:05.584Z",
  "payload": {
    "challengeId": 721,
    "userId": 132456,
    "type": "prize",
    "data": {
      "created": [
        {
          "place": 1,
          "projectId": 721,
          "prizeAmount": 100.00,
          "numberOfSubmissions": 1,
          "id": 101,
          "prizeType": {
            "id": 1,
            "description": ""
          }
        }
      ],
      "updated": [
        {
          "place": 2,
          "projectId": 721,
          "prizeAmount": 50.00,
          "numberOfSubmissions": 1,
          "id": 102,
          "prizeType": {
            "id": 2,
            "description": ""
          }
        }
      ],
      "deleted": [
        {
          "place": 1,
          "projectId": 721,
          "prizeAmount": 150.00,
          "numberOfSubmissions": 1,
          "id": 100,
          "prizeType": {
            "id": 1,
            "description": ""
          }
        }
      ]
    }
  }
}
```

### Project winner updated message

```json
{
  "topic": "or.notification.create",
  "originator": "tc-online-review",
  "mime-type": "application/json",
  "timestamp": "2022-05-31T06:15:05.584Z",
  "payload": {
    "challengeId": 721,
    "userId": 132456,
    "type": "winner",
    "data": "111999"
  }
}
```

### Project payment updated message

```json
{
  "topic": "or.notification.create",
  "originator": "tc-online-review",
  "mime-type": "application/json",
  "timestamp": "2022-05-31T06:15:05.584Z",
  "payload": {
    "challengeId": 721,
    "userId": 132456,
    "type": "payments",
    "data": [
      {
        "projectPaymentId": 101,
        "resourceId": 110,
        "submissionId": 1,
        "amount": 1000.00,
        "pactsPaymentId": 100,
        "createUser": "132567",
        "createDate": "2022-05-31T06:15:05.584Z",
        "modifyUser": "132567",
        "modifyDate": "2022-05-31T06:15:05.584Z",
        "projectPaymentType": {
          "projectPaymentTypeId": 1,
          "name": "type",
          "mergeable": true,
          "pactsPaymentTypeId": 100
        }
      }
    ]
  }
}
```

### Project timeline updated message

```json
{
  "topic": "or.notification.create",
  "originator": "tc-online-review",
  "mime-type": "application/json",
  "timestamp": "2022-05-31T06:15:05.584Z",
  "payload": {
    "challengeId": 721,
    "userId": 132456,
    "type": "timeline",
    "data": [
      {
        "name": "Registration",
        "scheduledStartDate": "2022-05-31T06:15:05.584Z",
        "scheduledEndDate": "2022-06-01T06:15:05.584Z"
      }
    ]
  }
}
```

### Resources updated message

```json
{
  "topic": "or.notification.create",
  "originator": "tc-online-review",
  "mime-type": "application/json",
  "timestamp": "2022-05-31T06:15:04.203Z",
  "payload": {
    "challengeId": 721,
    "userId": 132456,
    "type": "resources",
    "data": [
      {
        "creationUser": "132456",
        "creationTimestamp": "2010-05-21T11:26:30.000Z",
        "modificationUser": "132456",
        "modificationTimestamp": "2022-05-31T06:14:59.099Z",
        "id": 721,
        "resourceRole": {
          "creationUser": "System",
          "creationTimestamp": "2006-11-03T01:14:24.000Z",
          "modificationUser": "System",
          "modificationTimestamp": "2006-11-03T01:14:24.000Z",
          "id": 13,
          "name": "Manager",
          "description": "Manager",
          "phaseType": null
        },
        "project": 721,
        "phase": null,
        "submissions": [],
        "userId": 132456,
        "allProperties": {
          "External Reference ID": "132456",
          "Handle": "heffan"
        }
      }
    ]
  }
}
```

### Project Properties updated message

```json
{
  "topic": "or.notification.create",
  "originator": "tc-online-review",
  "mime-type": "application/json",
  "timestamp": "2022-05-31T06:15:05.584Z",
  "payload": {
    "challengeId": 721,
    "userId": 132456,
    "type": "properties",
    "data": {
      "Autopilot Option": "Off",
      "Billing Project": 0,
      "Eligibility": "Open",
      "Status Notification": "On",
      "Contest Indicator": "On",
      "Developer Forum ID": 0,
      "Payments": "0.0",
      "Public": "Yes",
      "Project Name": "Component 73",
      "External Reference ID": 1443,
      "Digital Run Flag": "On",
      "Component ID": 1441,
      "Rated": "Yes",
      "Root Catalog ID": "5801777",
      "Version ID": "1",
      "Project Version": "1.0                 ",
      "Timeline Notification": "On",
      "Send Winner Emails": "true",
      "Notes": "",
      "Approval Required": "true"
    }
  }
}
```

### Review info updated message

```json
{
  "topic": "or.notification.create",
  "originator": "tc-online-review",
  "mime-type": "application/json",
  "timestamp": "2022-05-31T06:15:05.584Z",
  "payload": {
    "challengeId": 721,
    "userId": 132456,
    "type": "review",
    "data": {
      "id": 1,
      "author": 123456,
      "submission": 100321,
      "projectPhase": -1,
      "scorecard": 123,
      "committed": false,
      "score": 98.01,
      "initialScore": 90.00,
      "comments": [
        {
          "id": 1,
          "author": 123456,
          "comment": "comment",
          "commentType": {
            "id": 1,
            "name": "commentType"
          }
        }
      ],
      "items": [
        {
          "id": 1,
          "answer": "answer",
          "question": 123,
          "document": 321,
          "comments": [
            {
              "id": 1,
              "author": 123456,
              "comment": "comment",
              "commentType": {
                "id": 1,
                "name": "commentType"
              }
            }
          ]
        }
      ],
      "creationUser": "System",
      "creationTimestamp": "2006-11-03T01:14:24.000Z",
      "modificationUser": "System",
      "modificationTimestamp": "2006-11-03T01:14:24.000Z"
    }
  }
}
```

### Review feedback updated message

```json
{
  "topic": "or.notification.create",
  "originator": "tc-online-review",
  "mime-type": "application/json",
  "timestamp": "2022-05-31T06:15:05.584Z",
  "payload": {
    "challengeId": 721,
    "userId": 132456,
    "type": "feedback",
    "data": [
      {
        "projectId": 721,
        "comment": "explanation",
        "details": [
          {
            "reviewerUserId": 132567,
            "score": 8,
            "feedbackText": "feedbackText"
          }
        ],
        "creationUser": "System",
        "creationTimestamp": "2006-11-03T01:14:24.000Z",
        "modificationUser": "System",
        "modificationTimestamp": "2006-11-03T01:14:24.000Z"
      }
    ]
  }
}
```
