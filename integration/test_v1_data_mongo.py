import unittest
import urllib2
import urllib
import json
from integration import *

class TestV1Data(unittest.TestCase):
  
  def test_get_plain(self): 
      url = '%s/Service/v1/data/integration_test/visitLayer' % SERVICE_URL

      req = urllib2.Request(url, headers={
          "Content-Type": "application/json",
          "Authorization": get_authorization()})
      f = urllib2.urlopen(req, context=get_context())

      self.assertEquals(200, f.code)

      body = f.read()
      
      self.assertTrue(len(body) > 0)
      
      bodyJson = json.loads(body)

  def test_get_larger_set(self):
      url = '%s/Service/v1/data/integration_test/dummy' % SERVICE_URL

      req = urllib2.Request(url, headers={
          "Content-Type": "application/json",
          "Authorization": get_authorization()})
      f = urllib2.urlopen(req, context=get_context())

      self.assertEquals(200, f.code)

      body = f.read()

      self.assertTrue(len(body) > 0)

      bodyJson = json.loads(body)

      self.assertTrue(len(bodyJson['items']) == 100)
      
  def test_get_larger_set_via_pagination(self):
      url = '%s/Service/v1/data/integration_test/dummy?pageSize=10' % SERVICE_URL
      items = []
      count = 0
      while (True):
        req = urllib2.Request(url, headers={
            "Content-Type": "application/json",
            "Authorization": get_authorization()})
        f = urllib2.urlopen(req, context=get_context())

        self.assertEquals(200, f.code)

        body = f.read()

        self.assertTrue(len(body) > 0)

        bodyJson = json.loads(body)
        items.extend(bodyJson['items'])
        if 'next' in bodyJson['links'] and 'href' in bodyJson['links']['next'] != None and count < 20:
          url = bodyJson['links']['next']['href']
          count += 1
        else:
          break

      self.assertTrue(len(items) == 100)

