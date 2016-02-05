import unittest
import urllib2
import urllib
import ssl
import base64
import json

from integration import *

class TestOAuth(unittest.TestCase):

  def obtain_token(self):
      data = urllib.urlencode({"grant_type":"client_credentials"})
      url = '%s/oauth-server/j_oauth_token_grant' % SERVICE_URL
      basicAuth = "Basic %s" % base64.encodestring('%s:%s' % (SERVICE_INTEGRATION_TEST_USER, SERVICE_INTEGRATION_TEST_PASSWORD)).replace('\n', '')
      req = urllib2.Request(url, data, {
          "Authorization": basicAuth})
      f = urllib2.urlopen(req, context=get_context())
      return json.loads(f.read())

  def test_obtain_token(self):
      token = self.obtain_token()

      self.assertTrue(len(token['access_token']) > 0)
      self.assertTrue(token['token_type'] == 'bearer')

  def test_use_token_on_v0(self): 
      token = self.obtain_token()
      url = '%s/Service/integration_test/layer/visit' % SERVICE_URL

      req = urllib2.Request(url, None, {
          "Content-Type": "application/json",
          "Authorization": "Bearer %s" % token['access_token']})
      f = urllib2.urlopen(req, context=get_context())

      self.assertEquals(200, f.code)

      body = f.read()
      
      self.assertTrue(len(body) > 0)
      
      bodyJson = json.loads(body)
      self.assertTrue(bodyJson[0]['measurementTimestamp'] > 0)
      
