CREATE TABLE event (
    storageDate       DATETIME2,
    category          NVARCHAR(100),
    action            NVARCHAR(100),
    stateName         NVARCHAR(100),
    stateId           NVARCHAR(100),
    messageId         UNIQUEIDENTIFIER,
    previousStateId   NVARCHAR(100),
    previousStateName NVARCHAR(100)
);
