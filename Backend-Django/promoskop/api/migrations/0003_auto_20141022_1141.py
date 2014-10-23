# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import models, migrations


class Migration(migrations.Migration):

    dependencies = [
        ('api', '0002_auto_20141022_1132'),
    ]

    operations = [
        migrations.RenameField(
            model_name='branch',
            old_name='store_id',
            new_name='store',
        ),
    ]
