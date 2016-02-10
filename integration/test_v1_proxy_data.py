import unittest
import urllib2
import urllib
import json
from integration import *

class TestV1Proxy(unittest.TestCase):

  def test_get_plain(self): 
      url = '%s/Service/v1/proxy/integration_test/test' % SERVICE_URL

      req = urllib2.Request(url, headers={
          "Authorization": get_authorization()})
      f = urllib2.urlopen(req, context=get_context())

      self.assertEquals(200, f.code)

      body = f.read()
     
      self.assertTrue(len(body) > 0)
      
      bodyJson = json.loads(body)
