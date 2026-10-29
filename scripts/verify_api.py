#!/usr/bin/env python3
"""Exercise real PostgreSQL-backed CRUD via HTTP; remove only this run's record."""
import json,sys,time
from urllib.request import Request,urlopen
from urllib.error import HTTPError,URLError
base=sys.argv[1] if len(sys.argv)>1 else 'http://127.0.0.1:8081'
def call(method,path,body=None,expected=200):
 data=None if body is None else json.dumps(body).encode()
 request=Request(base+path,data=data,method=method,headers={'Content-Type':'application/json'})
 try:
  with urlopen(request,timeout=10) as r:status,raw=r.status,r.read().decode()
 except HTTPError as r:status,raw=r.code,r.read().decode()
 print(method,path,'->',status,raw[:1200])
 assert status==expected,(method,path,status,expected)
 return json.loads(raw) if raw else None
for attempt in range(90):
 try:
  with urlopen(base+'/actuator/health',timeout=2) as r:
   if r.status==200:break
 except (URLError,TimeoutError):pass
 time.sleep(1)
else:raise SystemExit('Readiness timeout')
info=call('GET','/info');assert info['studentId']=='37773' and info['surname']=='Suyubek'
identifier='37773'+str(time.time_ns())[-9:]
body={'name':'Alisher','surname':'Suyubek','group':'IT2-2306','studentId':identifier,'email':'alisher@example.test'}
created=call('POST','/students',body,201);path='/students/'+str(created['id'])
try:
 rows=call('GET','/students');assert any(x['id']==created['id'] for x in rows)
 assert call('GET',path)['name']=='Alisher'
 call('POST','/students',body,409)
 body['group']='IT2-2306-UPDATED'
 assert call('PUT',path,body)['group']=='IT2-2306-UPDATED'
 bad=dict(body,name='');call('POST','/students',bad,400)
finally:
 call('DELETE',path,expected=204)
call('GET',path,expected=404)
call('PUT',path,body,404)
call('DELETE',path,expected=404)
print('PASS: owner identity, CREATE, LIST, GET, UPDATE, DELETE, validation, duplicate and absent-record responses')
