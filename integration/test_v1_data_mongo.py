import unittest
import urllib2
import urllib
import json
from integration import *

class TestV1Data(unittest.TestCase):
  
  def test_get_plain(self):
      f = self.request('Service/v1/data/integration_test/visitLayer')
      self.assertEquals(200, f.code)

      body = f.read()
      
      self.assertTrue(len(body) > 0)
      
      bodyJson = json.loads(body)

  def test_get_larger_set(self):
      f = self.request('Service/v1/data/integration_test/dummy')
      self.assertEquals(200, f.code)

      body = f.read()

      self.assertTrue(len(body) > 0)

      bodyJson = json.loads(body)

      self.assertTrue(len(bodyJson['items']) == 100)
      
  def test_get_larger_set_via_pagination(self):
      path = 'Service/v1/data/integration_test/dummy?pageSize=10'
      items = []
      count = 0
      while (True):
        f = self.request(path)
        self.assertEquals(200, f.code)

        body = f.read()

        self.assertTrue(len(body) > 0)

        bodyJson = json.loads(body)
        items.extend(bodyJson['items'])
        if 'next' in bodyJson['links'] and 'href' in bodyJson['links']['next'] != None and count < 20:
          path = bodyJson['links']['next']['href']
          count += 1
        else:
          break

      self.assertTrue(len(items) == 100)

  def test_post_measurement(self):
      single_measurement = '[{"test": "test"}]'
      f = self.request('Service/v1/data/integration_test/single_measurement', single_measurement)
      self.assertEquals(200, f.code)

      f = self.request('Service/v1/data/integration_test/single_measurement')
      self.assertEquals(200, f.code)

      body = json.loads(f.read())
      self.assertTrue(len(body['items']) == 1)
      self.assertTrue(body['items'][0]['test'] == 'test')

   
  def request(self, url, data=None, content_type="application/json"):
      if url[:8] != 'https://':
          url = '%s/%s' % (SERVICE_URL, url)

      req = urllib2.Request(url, data, headers={
          "Content-Type": content_type,
          "Authorization": get_authorization()})
      return urllib2.urlopen(req, context=get_context())







