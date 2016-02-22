import unittest
import json
import urllib2
from integration import *

class TestV1(unittest.TestCase):

  def test_options_on_root(self): 
    req = urllib2.Request('%s/Service/v1/' % SERVICE_URL, headers={
      "Authorization": get_authorization()})
    req.get_method = lambda: 'OPTIONS'
    f = urllib2.urlopen(req, context=get_context())

    self.assertEquals(200, f.code)

    headers = dict(f.headers)

    self.assertIn('access-control-allow-origin', headers)
    self.assertEquals(headers['access-control-allow-origin'], '*')

  def test_options_unknown_path(self):
    req = urllib2.Request('%s/Service/v1/unknown_path' % SERVICE_URL, headers={
      "Authorization": get_authorization()})
    req.get_method = lambda: 'OPTIONS'
    f = urllib2.urlopen(req, context=get_context())

    self.assertEquals(200, f.code)

    headers = dict(f.headers)

    self.assertIn('access-control-allow-origin', headers)
    self.assertEquals(headers['access-control-allow-origin'], '*')

  def test_options_variable_header(self):
    req = urllib2.Request('%s/Service/v1/unknown_path' % SERVICE_URL, headers={
      "Access-Control-Request-Headers": "unknown_header",
      "Authorization": get_authorization()})
    req.get_method = lambda: 'OPTIONS'
    f = urllib2.urlopen(req, context=get_context())

    self.assertEquals(200, f.code)

    headers = dict(f.headers)

    self.assertIn('access-control-allow-headers', headers)
    self.assertEquals(headers['access-control-allow-headers'], 'unknown_header')

  def test_options_variable_method(self):
    req = urllib2.Request('%s/Service/v1/unknown_path' % SERVICE_URL, headers={
      "Access-Control-Request-Method": "GET",
      "Authorization": get_authorization()})
    req.get_method = lambda: 'OPTIONS'
    f = urllib2.urlopen(req, context=get_context())

    self.assertEquals(200, f.code)

    headers = dict(f.headers)

    self.assertIn('access-control-allow-methods', headers)
    self.assertEquals(headers['access-control-allow-methods'], 'GET')


    




