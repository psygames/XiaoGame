cd %proto_bat_path%
f:
protoc  --proto_path=%proto_path% --java_out=%proto_path% %proto_path%/test/websocket/protobuf/proto/*.proto
pause