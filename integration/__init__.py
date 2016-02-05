import json 
import inspect
import re
import ssl
from datetime import datetime 
from glob import glob
from pymongo import MongoClient

SERVICE_URL = 'https://127.0.0.1:8443'
SERVICE_INTEGRATION_TEST_USER = 'integration_test_user'
SERVICE_INTEGRATION_TEST_PASSWORD = 'integration_test_user'


MONGODB_SERVER = '127.0.0.1'
MONGODB_PORT = 27017
MONGODB_USERNAME = 'admin'
MONGODB_PASSWORD = 'admin'
MONGODB_SECURITY_DATABASE = 'security'
MONGODB_APPLICATION_DATABASE = 'integration_test'
MONGODB_APPLICATION_USERNAME = 'application'
MONGODB_APPLICATION_PASSWORD = 'application'

def setup_package():
  client = MongoClient(MONGODB_SERVER)
  client.admin.authenticate(MONGODB_USERNAME, MONGODB_PASSWORD, mechanism='MONGODB-CR')

  security_database = client[MONGODB_SECURITY_DATABASE]
  application = security_database['applications'].find_one({'name': MONGODB_APPLICATION_DATABASE})

  if application is None:
    security_database['applications'].insert({
      'name': MONGODB_APPLICATION_DATABASE, 
      'database': { 
        'host': MONGODB_SERVER, 
        'port': MONGODB_PORT, 
        'username': MONGODB_APPLICATION_USERNAME, 
        'password': MONGODB_APPLICATION_PASSWORD, 
        'database': MONGODB_APPLICATION_DATABASE
      }
    })
    client[MONGODB_APPLICATION_DATABASE].add_user(MONGODB_APPLICATION_USERNAME, MONGODB_APPLICATION_PASSWORD, roles=['readWrite'])

  regx = re.compile("^integration_test.*", re.IGNORECASE)
  security_database['users'].remove({'roles':{'$elemMatch': { '$regex': regx}}})
  with open('fixture/security.users.json', 'r') as fp:
     users = json.load(fp)
     for user in users:
        security_database['users'].insert(user)


  application_database = client[MONGODB_APPLICATION_DATABASE]
  for collection in application_database.collection_names():
     if collection[:6] != 'system':
        application_database.drop_collection(collection)

  for fixture in glob('fixture/integration_test.*'):
     with open(fixture, 'r') as fp:
        collection = fixture[25:-5]
        for line in fp:
           document = json.loads(line)
           if '_id' in document: 
              del document['_id']
           application_database[collection].insert(fix_dates(document))

 
  FixtureLoadException('Couldn\'t load fixture') 

def fix_dates(document):
  for key, value in document.iteritems():
    if key == '$date':
      return datetime.strptime(value[0:19], '%Y-%m-%dT%H:%M:%S')
    else:
      if isinstance(value, dict): 
        result = fix_dates(value)
        if not isinstance(result, dict):
          document[key] = result
  return document

def get_context():
  ctx = ssl.create_default_context()
  ctx.check_hostname = False
  ctx.verify_mode = ssl.CERT_NONE
  return ctx 


class FixtureLoadException(Exception): 
  pass


