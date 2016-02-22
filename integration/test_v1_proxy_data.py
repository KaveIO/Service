import unittest
import urllib2
import urllib
import json
from integration import *

class TestV1Proxy(unittest.TestCase):

  def test_get_plain(self): 
      f = request('Service/v1/proxy/integration_test/test')
      self.assertEquals(200, f.code)
      body = f.read()
      self.assertTrue(len(body) > 0)
      bodyJson = json.loads(body)
