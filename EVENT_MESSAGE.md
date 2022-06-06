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
  "timestamp": "2022-06-06T01:57:28.854Z",
  "payload": {
    "challengeId": 181,
    "userId": 132456,
    "type": "prize",
    "data": [
      {
        "creationUser": "132456",
        "modificationUser": "132456",
        "creationTimestamp": "2022-06-06T01:57:26.915Z",
        "modificationTimestamp": "2022-06-06T01:57:26.915Z",
        "place": 1,
        "projectId": 181,
        "prizeAmount": 100,
        "prizeType": {
          "id": 15,
          "description": "Contest Prize"
        },
        "numberOfSubmissions": 1,
        "id": 21
      }
    ]
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
  "timestamp": "2022-06-06T05:51:06.373Z",
  "payload": {
    "challengeId": 171,
    "userId": 132456,
    "type": "timeline",
    "data": [
      {
        "scheduledEndDate": "2010-05-30T13:00:00.000Z",
        "actualEndDate": null,
        "name": "Registration",
        "actualStartDate": null,
        "attributes": {
          "Registration Number": "4",
          "Manual Screening": "No"
        },
        "phaseStatus": "Scheduled",
        "scheduledStartDate": "2010-05-27T13:00:00.000Z"
      },
      {
        "scheduledEndDate": "2010-06-03T13:00:00.000Z",
        "actualEndDate": null,
        "name": "Submission",
        "actualStartDate": null,
        "attributes": {
          "Submission Number": "0",
          "Manual Screening": "No"
        },
        "phaseStatus": "Scheduled",
        "scheduledStartDate": "2010-05-27T13:00:00.000Z"
      },
      {
        "scheduledEndDate": "2010-06-08T13:00:00.000Z",
        "actualEndDate": null,
        "name": "Screening",
        "actualStartDate": null,
        "attributes": {
          "Scorecard ID": "30000412",
          "Manual Screening": "No"
        },
        "phaseStatus": "Scheduled",
        "scheduledStartDate": "2010-06-07T13:00:00.000Z"
      },
      {
        "scheduledEndDate": "2010-06-12T13:00:00.000Z",
        "actualEndDate": null,
        "name": "Review",
        "actualStartDate": null,
        "attributes": {
          "Reviewer Number": "3",
          "Scorecard ID": "30000413",
          "Manual Screening": "No"
        },
        "phaseStatus": "Scheduled",
        "scheduledStartDate": "2010-06-08T13:00:00.000Z"
      },
      {
        "scheduledEndDate": "2010-06-15T13:00:00.000Z",
        "actualEndDate": null,
        "name": "Appeals",
        "actualStartDate": null,
        "attributes": {
          "View Response During Appeals": "No",
          "Manual Screening": "No"
        },
        "phaseStatus": "Scheduled",
        "scheduledStartDate": "2010-06-14T13:00:00.000Z"
      },
      {
        "scheduledEndDate": "2010-06-16T13:00:00.000Z",
        "actualEndDate": null,
        "name": "Appeals Response",
        "actualStartDate": null,
        "attributes": {
          "Manual Screening": "No"
        },
        "phaseStatus": "Scheduled",
        "scheduledStartDate": "2010-06-15T13:00:00.000Z"
      },
      {
        "scheduledEndDate": "2010-06-17T01:00:00.000Z",
        "actualEndDate": null,
        "name": "Aggregation",
        "actualStartDate": null,
        "attributes": {
          "Manual Screening": "No"
        },
        "phaseStatus": "Scheduled",
        "scheduledStartDate": "2010-06-16T13:00:00.000Z"
      },
      {
        "scheduledEndDate": "2010-06-17T13:00:00.000Z",
        "actualEndDate": null,
        "name": "Aggregation Review",
        "actualStartDate": null,
        "attributes": {
          "Manual Screening": "No"
        },
        "phaseStatus": "Scheduled",
        "scheduledStartDate": "2010-06-17T01:00:00.000Z"
      },
      {
        "scheduledEndDate": "2010-06-20T13:00:00.000Z",
        "actualEndDate": null,
        "name": "Final Fix",
        "actualStartDate": null,
        "attributes": {
          "Manual Screening": "No"
        },
        "phaseStatus": "Scheduled",
        "scheduledStartDate": "2010-06-17T13:00:00.000Z"
      },
      {
        "scheduledEndDate": "2010-06-23T13:00:00.000Z",
        "actualEndDate": null,
        "name": "Final Review",
        "actualStartDate": null,
        "attributes": {
          "Manual Screening": "No"
        },
        "phaseStatus": "Scheduled",
        "scheduledStartDate": "2010-06-22T13:00:00.000Z"
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
