import requests

url = 'http://139.196.5.96/proto/gen?type=file&out_type=text'
f = open("AssignRoomReply.proto", 'rb')
content = f.read()
f.close()
data = {
    'text': content
}
files = {'file': open("AssignRoomReply.proto", 'rb')}

response = requests.post(url, data=data, files=files)
print(response.content.decode('utf8'))
